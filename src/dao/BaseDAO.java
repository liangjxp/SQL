package dao;

import util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
封装通过操作
 */
public abstract class BaseDAO<T> {
    private Class<T> clazz = null;
    {
        // 获取当前BaseDAO子类继承的父类中的泛型
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;

        Type[] actualTypeArguments = paramType.getActualTypeArguments();
        clazz = (Class<T>) actualTypeArguments[0];
    }

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
    public <T> T getInstance(Connection connection, String sql, Object...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }

            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            if(rs.next()){
                T t = (T) clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(null,ps,rs);
        }
        return null;
    }
    public <T> List<T> getForList(Connection connection, String sql, Object...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            ArrayList<T> list = new ArrayList<>();
            while(rs.next()){
                T t = (T) clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(null,ps,rs);
        }
        return null;
    }
    // 查询特殊值
    public <T> T getValue(Connection connection, String sql, Object...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                return (T) rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(null, ps, rs);
        }
        return null;
    }
}
