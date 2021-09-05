package com.at.exer;

import com.at.guigu.connection.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Scanner;

/**
 * @author yh
 * @create 2021-09-03-19:46
 */
public class ExerTest1 {

    public static void main(String[] args) {
        ExerTest1 exerTest1 = new ExerTest1();
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("请输入用户名");
//        String name = scanner.next();
//        System.out.println("请输入邮箱");
//        String email = scanner.next();
//        System.out.println("请输入生日");
//        String birthday = scanner.next();
//        String sql = "insert into customers(name,email,birth) values(?,?,?)";
//        int insertCount = exerTest1.update(sql, name, email, birthday);
//        if (insertCount > 0) {
//            System.out.println("添加成功");
//        } else {
//            System.out.println("添加失败");
//        }


//        System.out.println("请选择您要输入的类型");
//        System.out.println("a.准考证号");
//        System.out.println("b.身份证号");
//        Scanner scanner = new Scanner(System.in);
//        String selection = scanner.next();
//        if("a".equalsIgnoreCase(selection)){
//            System.out.println("请输入准考证号：");
//            String examCard = scanner.next();
//            String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where examCard = ?";
//
//            Student student = exerTest1.getInstance(Student.class,sql,examCard);
//            if(student != null){
//                System.out.println(student);
//
//            }else{
//                System.out.println("输入的准考证号有误！");
//            }
//        }else if("a".equalsIgnoreCase(selection)){
//            System.out.println("请输入身份证号：");
//            String IDCard = scanner.next();
//            String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where IDCard = ?";
//
//            Student student = exerTest1.getInstance(Student.class,sql,IDCard);
//            if(student != null){
//                System.out.println(student);
//
//            }else{
//                System.out.println("输入的身份证号有误！");
//            }
//        }else {
//            System.out.println("您的输入有误！");
//        }

        System.out.println("请输入学生的准考证：");
        Scanner scanner = new Scanner(System.in);
        String next = scanner.next();
//        String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where examCard = ?";
        String sql = "delete from examstudent where examCard = ?";
        int deleteCount = exerTest1.update(sql,next);
        if(deleteCount > 0){
            System.out.println("删除成功");
        }else {
            System.out.println("查无此人");
        }
//        Student student = exerTest1.getInstance(Student.class, sql, next);
//        if(student == null){
//            System.out.println("查无此人");
//        }else {
//            String sql1 = "delete from examstudent where examCard = ?";
//            int deleteCount = exerTest1.update(sql1,next);
//            if(deleteCount > 0){
//                System.out.println("删除成功");
//            }
//        }
    }

    public <T> T getInstance(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
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
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }

    //通用的增删改操作
    public int update(String sql, Object ...args){//sql中占位符的个数与可变形参的长度相同！
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for(int i = 0;i < args.length;i++){
                ps.setObject(i + 1, args[i]);//小心参数声明错误！！
            }
            //4.执行
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //5.资源的关闭
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }


}
