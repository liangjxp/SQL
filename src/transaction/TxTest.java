package transaction;

import org.junit.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TxTest {
    // DML默认情况下，执行就会自动提交
    // set autocommit = false

    @Test
    public void testUpdateWithTx(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            System.out.println(connection.getAutoCommit());
            // 取消数据的自动提交
            connection.setAutoCommit(false);

            // 网络故障模拟
//            System.out.println(100 / 0);

            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            update(connection, sql1, "AA");

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(connection, sql2, "BB");

            System.out.println("转账成功");
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResources(connection, null);
        }
     }

    // 考虑事务，对于Connection，不用关闭
    public void update(Connection connection, String sql, Object ... args) {
        // sql中占位符个数与args数组长度一定相同
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
            JDBCUtils.closeResources(null, ps);
        }
    }
}
