package com.supermarket.ui;

import com.supermarket.entity.User;
import com.supermarket.service.UserService;
import com.supermarket.util.MD5Util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

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
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> showUserDialog(null));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "请先选择要修改的用户"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            showUserDialog(userService.findByUsername((String) tableModel.getValueAt(row, 1)));
        });
        btnDelete.addActionListener(e -> doDelete());
        btnRefresh.addActionListener(e -> refreshTable());
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<User> all = userService.findAll();
        for (User u : all) {
            tableModel.addRow(new Object[]{
                    u.getId(), u.getUsername(), u.getRole(),
                    u.getPhone() == null ? "" : u.getPhone(),
                    u.getStatus() == 1 ? "启用" : "禁用"
            });
        }
    }

    // ==================== 新增/修改对话框 ====================

    private void showUserDialog(User user) {
        boolean isNew = (user == null);

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"员工", "管理员"});
        JTextField phoneField = new JTextField(15);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"启用", "禁用"});

        if (!isNew) {
            usernameField.setText(user.getUsername());
            usernameField.setEnabled(false); // 用户名不可改
            roleCombo.setSelectedItem(user.getRole());
            phoneField.setText(user.getPhone());
            statusCombo.setSelectedIndex(user.getStatus() == 1 ? 0 : 1);
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("用户名："), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel(isNew ? "密码：" : "新密码（留空不修改）："), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("角色："), gbc);
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("手机号："), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("状态："), gbc);
        gbc.gridx = 1;
        panel.add(statusCombo, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel,
                isNew ? "新增用户" : "修改用户", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        // 校验
        String username = usernameField.getText().trim();
        if (username.isEmpty()) { JOptionPane.showMessageDialog(this, "用户名不能为空"); return; }

        if (isNew) {
            String pwd = new String(passwordField.getPassword());
            if (pwd.isEmpty()) { JOptionPane.showMessageDialog(this, "密码不能为空"); return; }

            if (userService.findByUsername(username) != null) {
                JOptionPane.showMessageDialog(this, "用户名已存在"); return;
            }

            User newUser = new User(username, pwd, (String) roleCombo.getSelectedItem());
            newUser.setPhone(phoneField.getText().trim());
            newUser.setStatus(statusCombo.getSelectedIndex() == 0 ? 1 : 0);

            if (userService.addUser(newUser)) {
                JOptionPane.showMessageDialog(this, "✅ 用户添加成功");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "❌ 添加失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // 修改
            String pwd = new String(passwordField.getPassword());
            if (!pwd.isEmpty()) {
                user.setPassword(MD5Util.encrypt(pwd));
            }
            user.setRole((String) roleCombo.getSelectedItem());
            user.setPhone(phoneField.getText().trim());
            user.setStatus(statusCombo.getSelectedIndex() == 0 ? 1 : 0);

            if (userService.updateUser(user)) {
                JOptionPane.showMessageDialog(this, "✅ 用户修改成功");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "❌ 修改失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== 删除 ====================

    private void doDelete() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "请先选择要删除的用户"); return; }

        String username = (String) tableModel.getValueAt(row, 1);
        if ("admin".equals(username)) {
            JOptionPane.showMessageDialog(this, "❌ 不能删除管理员账号");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "确认删除用户「" + username + "」？", "删除确认", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (userService.deleteUser(id)) {
                JOptionPane.showMessageDialog(this, "✅ 已删除");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "❌ 删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
