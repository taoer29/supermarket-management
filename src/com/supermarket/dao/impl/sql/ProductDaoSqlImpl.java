package com.supermarket.dao.impl.sql;

import com.supermarket.dao.ProductDao;
import com.supermarket.entity.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoSqlImpl extends SqlDaoBase<Product> implements ProductDao {

    @Override public boolean add(Product p) {
        String sql = "INSERT INTO products (name,category_id,manufacturer,production_date,expiry_date,purchase_date," +
                     "cost_price,selling_price,stock,sales_volume,min_stock,is_deleted) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName()); ps.setObject(2, p.getCategoryId() == 0 ? null : p.getCategoryId(), Types.INTEGER);
            ps.setString(3, p.getManufacturer()); ps.setString(4, p.getProductionDate());
            ps.setString(5, p.getExpiryDate()); ps.setString(6, p.getPurchaseDate());
            ps.setBigDecimal(7, p.getCostPrice()); ps.setBigDecimal(8, p.getSellingPrice());
            ps.setInt(9, p.getStock()); ps.setInt(10, p.getSalesVolume());
            ps.setInt(11, p.getMinStock()); ps.setInt(12, p.getIsDeleted());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) p.setId(rs.getInt(1)); }
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean update(Product p) {
        String sql = "UPDATE products SET name=?,category_id=?,manufacturer=?,production_date=?,expiry_date=?," +
                     "purchase_date=?,cost_price=?,selling_price=?,stock=?,sales_volume=?,min_stock=?,is_deleted=? WHERE id=?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName()); ps.setObject(2, p.getCategoryId() == 0 ? null : p.getCategoryId(), Types.INTEGER);
            ps.setString(3, p.getManufacturer()); ps.setString(4, p.getProductionDate());
            ps.setString(5, p.getExpiryDate()); ps.setString(6, p.getPurchaseDate());
            ps.setBigDecimal(7, p.getCostPrice()); ps.setBigDecimal(8, p.getSellingPrice());
            ps.setInt(9, p.getStock()); ps.setInt(10, p.getSalesVolume());
            ps.setInt(11, p.getMinStock()); ps.setInt(12, p.getIsDeleted());
            ps.setInt(13, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean delete(int id) {
        return updateDeletedStatus(id, 1);
    }

    @Override public boolean restore(int id) {
        return updateDeletedStatus(id, 0);
    }

    private boolean updateDeletedStatus(int id, int deleted) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("UPDATE products SET is_deleted=? WHERE id=?")) {
            ps.setInt(1, deleted); ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public Product findById(int id) {
        String sql = "SELECT * FROM products WHERE id=?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override public List<Product> findAll() {
        return queryList("SELECT * FROM products WHERE is_deleted=0");
    }

    @Override public List<Product> findByPage(int page, int pageSize) {
        return queryByPage("SELECT * FROM products WHERE is_deleted=0", page, pageSize, this::map);
    }

    @Override public int getTotalCount() { return getTotalCount("products"); }

    @Override public List<Product> searchByName(String keyword) {
        return queryList("SELECT * FROM products WHERE is_deleted=0 AND name LIKE N'%" + keyword + "%'");
    }

    @Override public List<Product> findByCategoryId(int categoryId) {
        return queryList("SELECT * FROM products WHERE is_deleted=0 AND category_id=" + categoryId);
    }

    @Override public List<Product> findLowStock() {
        return queryList("SELECT * FROM products WHERE is_deleted=0 AND stock <= min_stock");
    }

    @Override public List<Product> findDeleted() {
        return queryList("SELECT * FROM products WHERE is_deleted=1");
    }

    @Override public boolean updateStock(int id, int quantity) {
        String sql = "UPDATE products SET stock = stock + ? WHERE id=?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity); ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private List<Product> queryList(String sql) {
        List<Product> list = new ArrayList<>();
        try (Connection conn = getConn(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setManufacturer(rs.getString("manufacturer"));
        p.setProductionDate(rs.getString("production_date"));
        p.setExpiryDate(rs.getString("expiry_date"));
        p.setPurchaseDate(rs.getString("purchase_date"));
        p.setCostPrice(rs.getBigDecimal("cost_price"));
        p.setSellingPrice(rs.getBigDecimal("selling_price"));
        p.setStock(rs.getInt("stock"));
        p.setSalesVolume(rs.getInt("sales_volume"));
        p.setMinStock(rs.getInt("min_stock"));
        p.setIsDeleted(rs.getInt("is_deleted"));
        return p;
    }
}
