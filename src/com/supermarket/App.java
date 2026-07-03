package com.supermarket;

import com.supermarket.ui.LoginFrame;
import com.supermarket.util.MD5Util;

import javax.swing.*;

/**
 * 超市管理系统 - 启动入口
 *
 * 数据存储模式：由 util/Config.java 中的 DATA_MODE 控制
 * - FILE：文件I/O（序列化.dat文件，无需数据库）
 * - SQL：SQL Server（需安装 SQL Server 并运行 sql/init.sql）
 */
public class App {
    public static void main(String[] args) {
        // 设置跨平台UI风格
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 初始化默认管理员账号（首次运行时创建）
        initDefaultAdmin();

        // 启动登录窗口
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    /**
     * 首次运行创建默认管理员：admin / admin123
     */
    private static void initDefaultAdmin() {
        com.supermarket.service.UserService userService = new com.supermarket.service.UserService();
        if (userService.findByUsername("admin") == null) {
            com.supermarket.entity.User admin = new com.supermarket.entity.User("admin", "admin123", "管理员");
            userService.addUser(admin);
            System.out.println("默认管理员账号已创建：admin / admin123");
        }
    }
}
