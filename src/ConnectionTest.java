import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {

    @Test
    public void testConnection1() throws SQLException {
        Driver driver = new org.mariadb.jdbc.Driver();

        String url = "jdbc:mysql://10.4.49.97:3306/test";
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "root");

        Connection connect = driver.connect(url, info);

        System.out.println(connect);

    }

    @Test  // 采用反射获取Driver类实现对象，不出现第三方api，具有更好的移植性
    public void testConnection2() throws Exception {
        Class clazz = Class.forName("org.mariadb.jdbc.Driver");
        Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();

        String url = "jdbc:mysql://10.4.49.97:3306/test";
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "root");

        Connection connect = driver.connect(url, info);

        System.out.println(connect);
    }

    @Test  // 采用DriverManager替代Driver
    public void testConnection3() throws Exception {
        Class clazz = Class.forName("org.mariadb.jdbc.Driver");
        Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();

        String url = "jdbc:mysql://10.4.49.97:3306/test";
        String user = "root";
        String password = "root";
        // 注册驱动信息
        DriverManager.registerDriver(driver);

        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println(connection);
    }

    @Test  // 只用加载驱动，而不用显式注册，Driver实现类中静态代码块已经实现了注册
    public void testConnection4() throws Exception {
        String url = "jdbc:mysql://10.4.49.97:3306/test";
        String user = "root";
        String password = "root";

        Class.forName("org.mariadb.jdbc.Driver");

        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println(connection);
    }

    @Test  // final
    public void testConnection5() throws Exception {
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();

        pros.load(is);
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        Class.forName(driverClass);
        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println(connection);
    }
    /*
    实现了数据和代码的解耦，便于修改
     */
}
