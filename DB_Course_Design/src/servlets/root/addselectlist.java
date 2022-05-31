package servlets.root;

import DB.SelectListDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/addselectlist")
public class addselectlist extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String semester=request.getParameter("semester");
        String major=request.getParameter("major");
        String cnoset=request.getParameter("cno");
        String cno="("+cnoset.substring(1,cnoset.length()-1)+")";
        SelectListDB selectListDB=new SelectListDB();
        Boolean ok= true;
        Integer status=200;
        try {
            ok = selectListDB.add(semester,major,cno);
        } catch (SQLException e) {
            e.printStackTrace();
            status=500;
        }
        if(!ok)
            status=502;
        response.setStatus(status);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
