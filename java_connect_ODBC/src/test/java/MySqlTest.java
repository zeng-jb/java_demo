import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MySqlTest {
    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static String odbc_driver;
    private static String odbc_url;
    private static String username;
    private static String password;

    public static void main(String[] args) {
        // 测试
        String sql = "select * from students";
        System.out.println("ODBC************");
        ResultSet rst = MySqlTest.executeQuery(sql, null);
        try {
            while(rst.next()){
                System.out.print(rs.getString("sid") +"  ");
                System.out.print(rs.getString("sname")+"  ");
                System.out.print(rs.getString("age")+"  ");
                System.out.println(rs.getString("sex"));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            MySqlTest.close(MySqlTest.getConn(),MySqlTest.getPs(),rst);
        }
    }


    static{
        try {
            Map<String,String> map = get();
            odbc_driver  = map.get("odbc_driver");
            odbc_url = map.get("odbc_url");
            username = map.get("username");
            password = map.get("password");

            Class.forName(odbc_driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //初始化变量
    public static Map get(){
        Properties pp = new Properties();
        FileInputStream fis = null;
        Map<String,String> map = new HashMap<String, String>();
        try {
            fis = new FileInputStream("dbinfor.properties");//dbinfor.properties在工程路径下面
            pp.load(fis);
            odbc_driver = pp.getProperty("odbc_driver");
            odbc_url = pp.getProperty("odbc_url");
            username = pp.getProperty("username");
            password = pp.getProperty("password");
            map.put("odbc_driver", odbc_driver);
            map.put("odbc_url", odbc_url);
            map.put("username", username);
            map.put("password", password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    //增删改方法
    public static void executeUpdate(String sql,String []parameters){
        try {
            conn = DriverManager.getConnection(odbc_url, username, password);
            ps = conn.prepareStatement(sql);
            if(parameters!=null){
                for(int i=0;i<parameters.length;i++){
                    ps.setString(i+1, parameters[i]);
                }
            }
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally{
            MySqlTest.close(conn, ps, rs);
        }

    }

    //查询方法
    public static ResultSet executeQuery(String sql,String []parameters){
        try {
            conn = DriverManager.getConnection(odbc_url, username, password);
            ps = conn.prepareStatement(sql);
            if(parameters !=null){
                for(int i=0;i<parameters.length;i++){
                    ps.setString(i+1, parameters[i]);
                }
            }
            //执行查询
            rs = ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally{
            //关闭资源
            //SQLHelper.close(conn, ps, rs);
        }
        return rs;
    }
    //关闭资源
    public static void close(Connection conn,Statement stmt,ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }

        if(stmt!=null){
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            stmt = null;
        }

        if(conn!=null){
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            conn=null;
        }
    }
    public static Connection getConn() {
        return conn;
    }

    public static PreparedStatement getPs() {
        return ps;
    }



}
