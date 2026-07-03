package com.supermarket.dao.impl.sql;

import com.supermarket.dao.CategoryDao;
import com.supermarket.entity.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoSqlImpl extends SqlDaoBase<Category> implements CategoryDao {

    @Override public boolean add(Category c) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean update(Category c) {
        String sql = "UPDATE categories SET name=? WHERE id=?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setInt(2, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean delete(int id) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("DELETE FROM categories WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public Category findById(int id) {
        String sql = "SELECT * FROM categories WHERE id=?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        try (Connection conn = getConn(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM categories")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override public List<Category> findByPage(int page, int pageSize) {
        return queryByPage("SELECT * FROM categories", page, pageSize, this::map);
    }

    @Override public int getTotalCount() { return getTotalCount("categories"); }

    @Override public Category findByName(String name) {
        String sql = "SELECT * FROM categories WHERE name=?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    private Category map(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        return c;
    }
}
