package dao;

import beans.Customer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CustomerDAOImplTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(CustomerDAOImpl.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    CustomerDAOImpl dao = new CustomerDAOImpl();
    @Test
    public void insert() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Customer customer = new Customer(1, "jin", "jin@126.com", new Date(1111111111));
            dao.insert(connection, customer);
            System.out.println("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, null);
        }
    }

    @Test
    public void deleteById() {
    }

    @Test
    public void update() {
    }

    @Test
    public void getCustomerById() {
    }

    @Test
    public void getAll() {
    }

    @Test
    public void getCount() {
    }

    @Test
    public void getMaxBirth() {
    }
}
