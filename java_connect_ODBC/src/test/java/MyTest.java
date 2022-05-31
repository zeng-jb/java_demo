import com.zeng.DB.DB;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyTest {
    @Test
    public void test1(){
        Connection connection = DB.getConnection();
        String sql = "select * from students";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.print(rs.getString("sid") +"  ");
                System.out.print(rs.getString("sname")+"  ");
                System.out.print(rs.getString("age")+"  ");
                System.out.println(rs.getString("sex"));
            }

            DB.close(connection,ps,rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    //事务测试
    @Test
    public void TestTransaction(){
        Connection connection = DB.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String sql1 = "insert into books (bookName,bookCounts,detail) values('C++',10,'123123')";
        String sql2 = "delete from books where bookID = 12";
        try {
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps1.executeUpdate();
            ps2.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try{
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DB.close(connection);
    }

    //连表查询，以leftjoin为例
    @Test
    public void Testjoin(){
        Connection connection = DB.getConnection();
        String sql = "select * from books left join reader on books.rid = reader.id";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println("bookID   bookName   bookCounts   detail   id   name");
            while (rs.next()){
                System.out.print(rs.getString("bookID") + "   \t");
                System.out.print(rs.getString("bookName")+ "   \t");
                System.out.print(rs.getString("bookCounts")+ "   \t");
                System.out.print(rs.getString("detail")+ "   \t");
                System.out.print(rs.getString("id")+ "   \t");
                System.out.println(rs.getString("name"));

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DB.close(connection);
        }

    }

    //索引支持
    @Test
    public void TestIndex(){
        try {
            Connection connection = DB.getConnection();
            String sql = "explain select * from STUDENT where sname like 'ez%'";
            String sql2 = "select * from STUDENT where sname like 'ez%'";
            PreparedStatement ps = connection.prepareStatement(sql);
            PreparedStatement ps2 = connection.prepareStatement(sql2);

            ResultSet rs = ps.executeQuery();
            ResultSet rs2 = ps2.executeQuery();

            //System.out.println(rs.getInt(10));

            while (rs2.next()){
                System.out.print(rs2.getString("sid") + "\t");
                System.out.print(rs2.getString("sname") + "\t");
                System.out.print(rs2.getString("email") + "\t");
                System.out.print(rs2.getString("grade") + "\t");
                System.out.println();
            }


            DB.close(connection,ps,rs);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
