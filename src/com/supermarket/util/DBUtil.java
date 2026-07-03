package com.supermarket.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类——SQL Server + Windows 身份认证
 *
 * 使用前：
 * 1. 确保 SQL Server 已安装并运行
 * 2. 确保 lib/mssql-jdbc.jar 已添加到项目依赖
 * 3. 运行 sql/init.sql 初始化数据库表
 */
public class DBUtil {

    // SQL Server + Windows 身份认证
    private static final String DB_URL =
            "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=supermarket;"
                    + "integratedSecurity=true;"
                    + "encrypt=false;"
                    + "trustServerCertificate=true;"
                    + "authentication=NotSpecified";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ mssql-jdbc 驱动未找到，请检查 lib/mssql-jdbc.jar 是否已添加到项目依赖", e);
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
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
