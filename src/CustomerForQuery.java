import beans.Customer;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

public class CustomerForQuery {
    public Customer queryForCustomer(String sql, Object ... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery();
            // 获取结果集的元数据，封装有列数等信息
            ResultSetMetaData rsmd = rs.getMetaData();
            int columeCount = rsmd.getColumnCount();
            if(rs.next()){
                Customer customer = new Customer();
                for (int i = 0; i < columeCount ; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    // 获取每个列的列名，通用方法考虑反射
                    String columnName = rsmd.getColumnName(i + 1);

                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer, columnValue);
                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection,ps,rs);
        }
        return null;
    }
    @Test
    public void testQueryForCustomer(){
        String sql = "select id, name, birth, email from customers where id = ?";
        Customer customer = queryForCustomer(sql, 13);
        System.out.println(customer);

        sql = "select name, email from customers where name = ?";
        Customer customer1 = queryForCustomer(sql,  "周杰伦");
        System.out.println(customer1);
    }

    @Test
    public void testQuery1(){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id=?";
            ps = connection.prepareStatement(sql);

            ps.setObject(1,1);

            resultSet = ps.executeQuery();

            // 处理结果集
            if(resultSet.next()){  // 判断是否有数据
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

            // 方式一：输出
            // 方式二：封装为数组
            // 方式三：ORM，封装为对象
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, ps, resultSet);
        }
    }
}
