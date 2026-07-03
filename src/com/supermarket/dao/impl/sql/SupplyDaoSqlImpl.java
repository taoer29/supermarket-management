package com.supermarket.dao.impl.sql;

import com.supermarket.dao.SupplyDao;
import com.supermarket.entity.Supply;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplyDaoSqlImpl extends SqlDaoBase<Supply> implements SupplyDao {

    @Override public boolean add(Supply s) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO supplies (product_id,supplier_id) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getProductId()); ps.setInt(2, s.getSupplierId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) s.setId(rs.getInt(1)); }
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean update(Supply s) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE supplies SET product_id=?,supplier_id=? WHERE id=?")) {
            ps.setInt(1, s.getProductId()); ps.setInt(2, s.getSupplierId()); ps.setInt(3, s.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean delete(int id) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("DELETE FROM supplies WHERE id=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean deleteByProductId(int productId) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("DELETE FROM supplies WHERE product_id=?")) {
            ps.setInt(1, productId); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public Supply findById(int id) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM supplies WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override public List<Supply> findAll() {
        List<Supply> list = new ArrayList<>();
        try (Connection conn = getConn(); Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM supplies")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override public List<Supply> findByPage(int page, int pageSize) {
        return queryByPage("SELECT * FROM supplies", page, pageSize, this::map);
    }

    @Override public int getTotalCount() { return getTotalCount("supplies"); }

    @Override public List<Supply> findByProductId(int productId) {
        return queryList("SELECT * FROM supplies WHERE product_id=" + productId);
    }

    @Override public List<Supply> findBySupplierId(int supplierId) {
        return queryList("SELECT * FROM supplies WHERE supplier_id=" + supplierId);
    }

    private List<Supply> queryList(String sql) {
        List<Supply> list = new ArrayList<>();
        try (Connection conn = getConn(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Supply map(ResultSet rs) throws SQLException {
        Supply s = new Supply();
        s.setId(rs.getInt("id")); s.setProductId(rs.getInt("product_id"));
        s.setSupplierId(rs.getInt("supplier_id"));
        return s;
    }
}
