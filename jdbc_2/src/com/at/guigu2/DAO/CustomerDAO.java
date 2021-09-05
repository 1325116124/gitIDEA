package com.at.guigu2.DAO;

import com.at.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author yh
 * @create 2021-09-04-20:00
 */
public interface CustomerDAO {
    /**
     * @Description 将cust对象添加到数据库中
     * @param conn
     * @param cust
     */
    void insert(Connection conn, Customer cust);
    void deleteById(Connection conn,int id);
    void update(Connection conn,Customer cust);
    Customer getCustomerById(Connection conn,int id);
    List<Customer> getAll(Connection conn);
    Long getCount(Connection conn);
    Date getMaxBirth(Connection conn);
}
