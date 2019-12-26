import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class PreparedStatementTest {
    // 通用修改操作
    public void update(String sql, Object ... args) {
        // sql中占位符个数与args数组长度一定相同
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, ps);
        }
    }
    @Test
    public void updateTest(){
//        String sql = "delete from customers where id = ?";
//        update(sql, 20);
        // 当sql语句中出现关键字信息时，会报错，如下的order，需要采用如图所示处理
        String sql = "update `order` set order_name = ? where order_id = ?";
        update(sql, "DD", 2);
    }

    @Test
    public void testUpdate() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();

            String sql = "update customers set name = ? where id = ?";
            ps = connection.prepareStatement(sql);
            // 通用写法
            ps.setObject(1, "莫扎特");
            ps.setObject(2, 18);

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, ps);
        }
    }
    @Test
    public void testInsert() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
            Properties pros = new Properties();

            pros.load(is);
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, user, password);

            System.out.println(connection);
            // 预编译sql语句，返回PreparedStatement实例
            String sql = "insert into customers(name, email, birth) values(?,?,?)";
            ps = connection.prepareStatement(sql);
            // 填充占位符
            ps.setString(1, "哪吒");
            ps.setString(2, "nezha@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("2019-12-01");
            ps.setDate(3, new Date(date.getTime()));
            // 执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}