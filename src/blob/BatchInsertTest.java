package blob;

import org.junit.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BatchInsertTest {
    @Test
    public void testInsert3() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();

            connection = JDBCUtils.getConnection();
            connection.setAutoCommit(false);
            String sql = "insert into goods(name) values(?)";
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < 100000; i++) {
                ps.setObject(1, "name_"+i);

                ps.addBatch();
                if(i % 500 == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            connection.commit();
            long end = System.currentTimeMillis();
            System.out.println("执行时间: " + (end - start));    // 1347
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, ps);
        }
    }

    @Test
    public void testInsert2() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();

            connection = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < 100000; i++) {
                ps.setObject(1, "name_"+i);

                ps.addBatch();
                if(i % 500 == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("执行时间: " + (end - start));    // 1568
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, ps);
        }
    }

    @Test
    public void testInsert1() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();

            connection = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < 20000; i++) {
                ps.setObject(1, "name_"+i);
                ps.execute();
            }

            long end = System.currentTimeMillis();
            System.out.println("执行时间: " + (end - start));    // 25865
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection, ps);
        }
    }
}
