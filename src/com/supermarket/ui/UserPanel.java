package com.supermarket.ui;

import com.supermarket.entity.User;
import com.supermarket.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * 用户管理面板——管理员管理员工账号
 */
public class UserPanel extends JPanel {
    private final UserService userService = new UserService();
    private JTable table;
    private DefaultTableModel tableModel;

    public UserPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("新增用户");
        JButton btnEdit = new JButton("修改用户");
        JButton btnDelete = new JButton("删除用户");
        JButton btnRefresh = new JButton("刷新");

        toolBar.add(btnAdd);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);
        toolBar.add(btnRefresh);
        add(toolBar, BorderLayout.NORTH);

        String[] columns = {"编号", "用户名", "角色", "手机号", "状态"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> refreshTable());
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (User u : userService.findAll()) {
            tableModel.addRow(new Object[]{
                    u.getId(), u.getUsername(), u.getRole(),
                    u.getPhone(), u.getStatus() == 1 ? "启用" : "禁用"
            });
        }
    }
}
