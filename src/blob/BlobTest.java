package blob;

import beans.Customer;
import org.junit.Test;
import util.JDBCUtils;

import java.io.*;
import java.sql.*;

public class BlobTest {
    @Test
    public void testQuery(){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select id, name, email, birth, photo from customers where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, 22);
            rs = ps.executeQuery();
            is = null;
            fos = null;
            if(rs.next()){
    //            int id = rs.getInt(1);
    //            String name = rs.getString(2);
    //            String email = rs.getString(3);
    //            Date birth = rs.getDate(4);

                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);

                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("download-girl.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while((len = is.read(buffer)) != -1){
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResources(connection, ps, rs);
        }
    }

    @Test
    public void testInsert() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "insert into customers(name, email, birth, photo) values(?,?,?,?)";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, "feishan");
            ps.setObject(2, "jin@qq.com");
            ps.setObject(3, "1992-01-01");
            FileInputStream is = new FileInputStream(new File("src/resources/big.jpg"));
            ps.setBlob(4, is);

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection,ps);
        }
    }
}
