package com.at.guigu.connection;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author yh
 * @create 2021-09-02-16:30
 */
public class ConnectionTest {
    @Test
    public void test() throws SQLException {
        Driver driver = new com.mysql.jdbc.Driver();
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8";
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","yanghong");
        Connection conn = driver.connect(url,info);
        System.out.println(conn);
    }

    @Test
    public void test2() throws Exception {
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "yanghong");
        Connection conn = driver.connect(url,info);
    }

    @Test
    public void test3() throws Exception {
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "yanghong";

        DriverManager.registerDriver(driver);
        Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println(conn);
    }

    @Test
    public void test4() throws Exception {
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "yanghong";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println(conn);
    }

    @Test
    public void test5() throws Exception {
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        pros.load(is);
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");
        Class.forName(driverClass);
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }
}
