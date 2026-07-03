package com.supermarket.dao.impl.sql;

import com.supermarket.dao.PurchaseDao;
import com.supermarket.entity.Purchase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDaoSqlImpl extends SqlDaoBase<Purchase> implements PurchaseDao {

    @Override public boolean add(Purchase p) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO purchases (product_id,supplier_id,quantity,unit_price,total_price,purchase_date,operator_id) VALUES (?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getProductId()); ps.setObject(2, p.getSupplierId() == 0 ? null : p.getSupplierId(), Types.INTEGER);
            ps.setInt(3, p.getQuantity()); ps.setBigDecimal(4, p.getUnitPrice());
            ps.setBigDecimal(5, p.getTotalPrice()); ps.setString(6, p.getPurchaseDate());
            ps.setObject(7, p.getOperatorId() == 0 ? null : p.getOperatorId(), Types.INTEGER);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) p.setId(rs.getInt(1)); }
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean update(Purchase p) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE purchases SET product_id=?,supplier_id=?,quantity=?,unit_price=?,total_price=?,purchase_date=?,operator_id=? WHERE id=?")) {
            ps.setInt(1, p.getProductId()); ps.setObject(2, p.getSupplierId() == 0 ? null : p.getSupplierId(), Types.INTEGER);
            ps.setInt(3, p.getQuantity()); ps.setBigDecimal(4, p.getUnitPrice());
            ps.setBigDecimal(5, p.getTotalPrice()); ps.setString(6, p.getPurchaseDate());
            ps.setObject(7, p.getOperatorId() == 0 ? null : p.getOperatorId(), Types.INTEGER);
            ps.setInt(8, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean delete(int id) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("DELETE FROM purchases WHERE id=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public Purchase findById(int id) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM purchases WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override public List<Purchase> findAll() {
        List<Purchase> list = new ArrayList<>();
        try (Connection conn = getConn(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM purchases ORDER BY id DESC")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override public List<Purchase> findByPage(int page, int pageSize) {
        return queryByPage("SELECT * FROM purchases", page, pageSize, this::map);
    }

    @Override public int getTotalCount() { return getTotalCount("purchases"); }

    @Override public List<Purchase> findByDateRange(String start, String end) {
        return queryList("SELECT * FROM purchases WHERE purchase_date >= '" + start + "' AND purchase_date <= '" + end + "' ORDER BY id DESC");
    }

    @Override public List<Purchase> findByProductId(int productId) {
        return queryList("SELECT * FROM purchases WHERE product_id=" + productId + " ORDER BY id DESC");
    }

    @Override public List<Purchase> findBySupplierId(int supplierId) {
        return queryList("SELECT * FROM purchases WHERE supplier_id=" + supplierId + " ORDER BY id DESC");
    }

    private List<Purchase> queryList(String sql) {
        List<Purchase> list = new ArrayList<>();
        try (Connection conn = getConn(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Purchase map(ResultSet rs) throws SQLException {
        Purchase p = new Purchase();
        p.setId(rs.getInt("id")); p.setProductId(rs.getInt("product_id"));
        p.setSupplierId(rs.getInt("supplier_id")); p.setQuantity(rs.getInt("quantity"));
        p.setUnitPrice(rs.getBigDecimal("unit_price")); p.setTotalPrice(rs.getBigDecimal("total_price"));
        p.setPurchaseDate(rs.getString("purchase_date")); p.setOperatorId(rs.getInt("operator_id"));
        return p;
    }
}
