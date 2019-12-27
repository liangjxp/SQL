package dao;

import beans.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/*
此接口用于规范针对customer表的常用操作
 */
public interface CustomerDAO {
    void insert(Connection connection, Customer customer);

    void deleteById(Connection connection, int id);

    void update(Connection connection, Customer customer);

    Customer getCustomerById(Connection connection, int id);

    List<Customer> getAll(Connection connection);

    Long getCount(Connection connection);

    Date getMaxBirth(Connection connection);
}
