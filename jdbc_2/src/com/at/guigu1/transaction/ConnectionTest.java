package com.at.guigu1.transaction;

import com.at.guigu1.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author yh
 * @create 2021-09-04-13:36
 */
public class ConnectionTest {

    public int update(String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for(int i = 0;i < args.length;i++) {
                ps.setObject(i+1,args[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }
        return 0;
    }

    @Test
    public void testUpdateWithTx(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            System.out.println(conn.getAutoCommit());
            conn.setAutoCommit(false);
            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            update(conn,sql1,"AA");
            System.out.println(10/0);
            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(conn,sql2,"BB");
            System.out.println("转账成功");
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException el){
                el.printStackTrace();
            }
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    // 通用的增删改操作---version 2.0 （考虑上事务）
    public int update(Connection conn,String sql, Object... args) {// sql中占位符的个数与可变形参的长度相同！
        PreparedStatement ps = null;
        try {
            // 1.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            // 2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);// 小心参数声明错误！！
            }
            // 3.执行
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 4.资源的关闭
            JDBCUtils.closeResource(null, ps);

        }
        return 0;

    }

    //通用的查询操作（考虑事务）
    public <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object ...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                T t = clazz.newInstance();
                for(int i=0;i<columnCount;i++){
                    Object columnValue = rs.getObject(i+1);
                    String columnName = rsmd.getColumnLabel(i+1);
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "update user_table set balance = ? where user = ?";
        conn.setAutoCommit(false);
        update(conn,sql,5000,"CC");
        Thread.sleep(15000);
        System.out.println("修改结束");
    }

    @Test
    public void testTransactionSelect() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "select user,password,balance from user_table where user = ?";
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        System.out.println(conn.getTransactionIsolation());
        conn.setAutoCommit(false);
        User user = getInstance(conn, User.class, sql, "CC");
        System.out.println(user);
    }
}
