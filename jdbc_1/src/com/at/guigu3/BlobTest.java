package com.at.guigu3;

import com.at.guigu.connection.Customer;
import com.at.guigu.connection.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.*;

/**
 * @author yh
 * @create 2021-09-03-23:36
 */
public class BlobTest {
    @Test
    public void test() throws Exception{
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1,"元昊");
        ps.setObject(2,"yuan@qq.com");
        ps.setObject(3,"2021-02-01");
        FileInputStream fis = new FileInputStream(new File("bizhi3.png"));
        ps.setBlob(4,fis);
        ps.execute();
        JDBCUtils.closeResource(conn,ps);
    }

    @Test
    public void test2(){
        Connection conn = null;
        PreparedStatement ps = null;
        InputStream is = null;
        FileOutputStream fos = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,21);
            rs = ps.executeQuery();
            if(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");
                Customer cust = new Customer(id,name,email,birth);
                System.out.println(cust);
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("bizhi.png");
                byte[] buffer = new byte[1024];
                int len;
                while((len = is.read(buffer)) != -1){
                    fos.write(buffer,0,len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
            try {
                if(is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
