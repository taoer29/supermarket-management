package com.supermarket.dao.impl.sql;

import com.supermarket.dao.SupplierDao;
import com.supermarket.entity.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoSqlImpl extends SqlDaoBase<Supplier> implements SupplierDao {

    @Override public boolean add(Supplier s) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO suppliers (name,phone,address) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getName()); ps.setString(2, s.getPhone()); ps.setString(3, s.getAddress());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) s.setId(rs.getInt(1)); }
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean update(Supplier s) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE suppliers SET name=?,phone=?,address=? WHERE id=?")) {
            ps.setString(1, s.getName()); ps.setString(2, s.getPhone()); ps.setString(3, s.getAddress());
            ps.setInt(4, s.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean delete(int id) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("DELETE FROM suppliers WHERE id=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public Supplier findById(int id) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM suppliers WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override public List<Supplier> findAll() {
        List<Supplier> list = new ArrayList<>();
        try (Connection conn = getConn(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM suppliers")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override public List<Supplier> findByPage(int page, int pageSize) {
        return queryByPage("SELECT * FROM suppliers", page, pageSize, this::map);
    }

    @Override public int getTotalCount() { return getTotalCount("suppliers"); }

    @Override public Supplier findByName(String name) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM suppliers WHERE name=?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    private Supplier map(ResultSet rs) throws SQLException {
        Supplier s = new Supplier();
        s.setId(rs.getInt("id")); s.setName(rs.getString("name"));
        s.setPhone(rs.getString("phone")); s.setAddress(rs.getString("address"));
        return s;
    }
}
