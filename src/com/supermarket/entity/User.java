package com.supermarket.entity;

import java.io.Serializable;

/**
 * 用户实体类
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String username;      // 用户名（唯一）
    private String password;      // 密码（MD5加密）
    private String role;          // 角色：管理员/员工
    private String phone;         // 手机号
    private int status;           // 状态：0-禁用 1-启用

    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = 1;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role='" + role + "'}";
    }
}
