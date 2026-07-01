package com.supermarket.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类——接入MySQL后使用
 * 当前文件存储阶段不需要数据库，直接返回null
 *
 * 接入MySQL时：
 * 1. 添加 mysql-connector-java 依赖
 * 2. 取消下方注释
 * 3. 修改 DB_URL, USER, PASSWORD 为实际配置
 */
public class DBUtil {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/supermarket?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    /**
     * 获取数据库连接（接入MySQL时使用）
     */
    public static Connection getConnection() throws SQLException {
        // 当前使用文件存储，不需要数据库
        // 接入MySQL时取消注释：
        // return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        throw new SQLException("当前使用文件存储模式，未接入数据库。切换到MySQL时取消 DBUtil 注释即可。");
    }

    /**
     * 关闭连接
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}
