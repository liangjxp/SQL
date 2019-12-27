package connectionpoll;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class C3P0Test {
    @Test
    public void testGetConnection() throws Exception {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("org.mariadb.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql://10.4.49.97:3306/test");
        cpds.setUser("root");
        cpds.setPassword("root");

        cpds.setInitialPoolSize(10);
        Connection connection = cpds.getConnection();
        System.out.println(connection);

    }
}
