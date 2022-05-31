package com.zeng.utils;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class DBCPUtils {
	
	 private static DataSource dataSource;

	static{
		Properties pp = new Properties();
		FileInputStream fis = null;
		   try {
			   fis = new FileInputStream("dbinfor.properties");//dbinfor.properties在工程路径下面
			   pp.load(fis);

			   dataSource=BasicDataSourceFactory.createDataSource(pp);
		} catch (Exception e) {

			e.printStackTrace();
		}
	   }
	public static DataSource getDataSource(){
		   return dataSource;
	   }
	   public static Connection getConnection(){
		   try {
			return dataSource.getConnection();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	   }

}
