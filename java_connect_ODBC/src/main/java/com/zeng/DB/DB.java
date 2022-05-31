package com.zeng.DB;

import java.io.FileInputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DB {

    private static String odbc_driver;
    private static String odbc_url;
    private static String username;
    private static String password;


    //初始化连接参数;
    static{
        try {
            Map<String,String> map = get();
            odbc_driver  = map.get("odbc_driver");
            odbc_url = map.get("odbc_url");
            username = map.get("username");
            password = map.get("password");

            //初始化驱动加载
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

    //连接
    public static Connection getConnection()
    {
        try {
            Connection connection=DriverManager.getConnection(odbc_url,username,password);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //关闭
    public static void close(Connection connection,Statement st,ResultSet rs)
    {
        try {
            connection.close();
            st.close();
            rs.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void close(Connection connection,PreparedStatement st)
    {
        try {
            connection.close();
            st.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void close(Statement st)
    {
        try {
            st.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void close(Connection connection)
    {
        try {
            connection.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void close(ResultSet resultSet)
    {
        try {
            resultSet.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

