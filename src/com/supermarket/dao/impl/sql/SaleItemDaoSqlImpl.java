package com.supermarket.dao.impl.sql;

import com.supermarket.dao.SaleItemDao;
import com.supermarket.entity.SaleItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleItemDaoSqlImpl extends SqlDaoBase<SaleItem> implements SaleItemDao {

    @Override public boolean add(SaleItem item) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO sale_items (sale_id,product_id,product_name,quantity,subtotal) VALUES (?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getSaleId()); ps.setInt(2, item.getProductId());
            ps.setString(3, item.getProductName()); ps.setInt(4, item.getQuantity());
            ps.setBigDecimal(5, item.getSubtotal());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) item.setId(rs.getInt(1)); }
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean update(SaleItem item) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE sale_items SET product_id=?,product_name=?,quantity=?,subtotal=? WHERE id=?")) {
            ps.setInt(1, item.getProductId()); ps.setString(2, item.getProductName());
            ps.setInt(3, item.getQuantity()); ps.setBigDecimal(4, item.getSubtotal());
            ps.setInt(5, item.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean delete(int id) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("DELETE FROM sale_items WHERE id=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public SaleItem findById(int id) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM sale_items WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override public List<SaleItem> findAll() {
        List<SaleItem> list = new ArrayList<>();
        try (Connection conn = getConn(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM sale_items")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override public List<SaleItem> findByPage(int page, int pageSize) {
        return queryByPage("SELECT * FROM sale_items", page, pageSize, this::map);
    }

    @Override public int getTotalCount() { return getTotalCount("sale_items"); }

    @Override public List<SaleItem> findBySaleId(int saleId) {
        List<SaleItem> list = new ArrayList<>();
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM sale_items WHERE sale_id=?")) {
            ps.setInt(1, saleId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private SaleItem map(ResultSet rs) throws SQLException {
        SaleItem item = new SaleItem();
        item.setId(rs.getInt("id")); item.setSaleId(rs.getInt("sale_id"));
        item.setProductId(rs.getInt("product_id")); item.setProductName(rs.getString("product_name"));
        item.setQuantity(rs.getInt("quantity")); item.setSubtotal(rs.getBigDecimal("subtotal"));
        return item;
    }
}
