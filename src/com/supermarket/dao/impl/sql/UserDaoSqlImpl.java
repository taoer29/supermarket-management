package com.supermarket.dao.impl.sql;

import com.supermarket.dao.UserDao;
import com.supermarket.entity.User;
import com.supermarket.util.MD5Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoSqlImpl extends SqlDaoBase<User> implements UserDao {

    @Override public boolean add(User u) {
        String sql = "INSERT INTO users (username, password, role, phone, status) VALUES (?,?,?,?,?)";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, MD5Util.encrypt(u.getPassword()));
            ps.setString(3, u.getRole());
            ps.setString(4, u.getPhone());
            ps.setInt(5, u.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) u.setId(rs.getInt(1)); }
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean update(User u) {
        String sql = "UPDATE users SET role=?, phone=?, status=? WHERE id=?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getRole());
            ps.setString(2, u.getPhone());
            ps.setInt(3, u.getStatus());
            ps.setInt(4, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean delete(int id) {
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id=?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try (Connection conn = getConn(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM users")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override public List<User> findByPage(int page, int pageSize) {
        return queryByPage("SELECT * FROM users", page, pageSize, this::map);
    }

    @Override public int getTotalCount() { return getTotalCount("users"); }

    @Override public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=? AND status=1";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, MD5Util.encrypt(password));
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setPhone(rs.getString("phone"));
        u.setStatus(rs.getInt("status"));
        return u;
    }
}
