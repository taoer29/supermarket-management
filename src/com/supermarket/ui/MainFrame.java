package com.supermarket.ui;

import com.supermarket.entity.User;

import javax.swing.*;
import java.awt.*;

/**
 * 主窗口——使用JTabbedPane切换功能模块
 */
public class MainFrame extends JFrame {
    private final User currentUser;

    public MainFrame(User user) {
        this.currentUser = user;
        setTitle("超市管理系统 - 当前用户：" + user.getUsername() + " (" + user.getRole() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        tabbedPane.addTab("商品管理", new ProductPanel());
        tabbedPane.addTab("进货管理", new PurchasePanel(currentUser));
        tabbedPane.addTab("用户管理", new UserPanel());
        tabbedPane.addTab("供应商管理", new SupplierPanel());
        tabbedPane.addTab("收银台", new SalePanel(currentUser));
        tabbedPane.addTab("报表统计", new ReportPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // 底部状态栏
        JLabel statusBar = new JLabel(" 已连接到数据存储：文件模式（可切换至MySQL）");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        add(statusBar, BorderLayout.SOUTH);
    }
}
