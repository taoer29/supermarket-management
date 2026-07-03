package com.supermarket.dao.impl.sql;

import com.supermarket.dao.SaleDao;
import com.supermarket.entity.Sale;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDaoSqlImpl extends SqlDaoBase<Sale> implements SaleDao {

    @Override public boolean add(Sale s) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO sales (total_amount,sale_time,operator_id) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setBigDecimal(1, s.getTotalAmount()); ps.setString(2, s.getSaleTime()); ps.setInt(3, s.getOperatorId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) s.setId(rs.getInt(1)); }
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean update(Sale s) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE sales SET total_amount=?,sale_time=?,operator_id=? WHERE id=?")) {
            ps.setBigDecimal(1, s.getTotalAmount()); ps.setString(2, s.getSaleTime()); ps.setInt(3, s.getOperatorId());
            ps.setInt(4, s.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean delete(int id) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("DELETE FROM sales WHERE id=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public Sale findById(int id) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM sales WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override public List<Sale> findAll() {
        List<Sale> list = new ArrayList<>();
        try (Connection conn = getConn(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM sales ORDER BY id DESC")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override public List<Sale> findByPage(int page, int pageSize) {
        return queryByPage("SELECT * FROM sales", page, pageSize, this::map);
    }

    @Override public int getTotalCount() { return getTotalCount("sales"); }

    @Override public List<Sale> findByDateRange(String start, String end) {
        List<Sale> list = new ArrayList<>();
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM sales WHERE sale_time >= ? AND sale_time <= ? ORDER BY id DESC")) {
            ps.setString(1, start); ps.setString(2, end);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override public List<Sale> findByOperatorId(int operatorId) {
        List<Sale> list = new ArrayList<>();
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM sales WHERE operator_id=? ORDER BY id DESC")) {
            ps.setInt(1, operatorId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Sale map(ResultSet rs) throws SQLException {
        Sale s = new Sale();
        s.setId(rs.getInt("id")); s.setTotalAmount(rs.getBigDecimal("total_amount"));
        s.setSaleTime(rs.getString("sale_time")); s.setOperatorId(rs.getInt("operator_id"));
        return s;
    }
}
