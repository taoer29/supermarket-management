package com.supermarket.dao.impl.sql;

import com.supermarket.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL Server DAO 基类——封装连接获取、资源关闭、分页查询等通用逻辑
 */
public abstract class SqlDaoBase<T> {

    /**
     * 获取新连接（每次调用独立连接，Service层可自行管理事务）
     */
    protected Connection getConn() throws SQLException {
        return DBUtil.getConnection();
    }

    /**
     * 关闭资源（Connection由调用方管理）
     */
    protected void close(ResultSet rs, Statement stmt) {
        try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
        try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
    }

    /**
     * 查询全部（带分页）
     */
    protected List<T> queryByPage(String baseSql, int page, int pageSize, RowMapper<T> mapper) {
        String sql = "SELECT * FROM (" + baseSql + ") AS t " +
                     "ORDER BY (SELECT NULL) " +
                     "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<T> list = new ArrayList<>();
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (page - 1) * pageSize);
            ps.setInt(2, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapper.map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取总记录数
     */
    protected int getTotalCount(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}
