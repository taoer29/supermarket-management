package com.supermarket.ui;

import com.supermarket.entity.User;
import com.supermarket.service.UserService;

import javax.swing.*;
import java.awt.*;

/**
 * 登录窗口
 */
public class LoginFrame extends JFrame {
    private final UserService userService = new UserService();
    private JTextField usernameField;
    private JPasswordField passwordField;
    private int attemptCount = 0;

    public LoginFrame() {
        setTitle("超市管理系统 - 登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // 标题
        JLabel titleLabel = new JLabel("超市管理系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // 用户名
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        panel.add(new JLabel("用户名："), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // 密码
        gbc.gridy = 2; gbc.gridx = 0;
        panel.add(new JLabel("密  码："), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // 登录按钮
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        JButton loginBtn = new JButton("登 录");
        loginBtn.setPreferredSize(new Dimension(200, 35));
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);
        panel.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> doLogin());
        passwordField.addActionListener(e -> doLogin());

        add(panel);
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名和密码");
            return;
        }

        User user = userService.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "登录成功！欢迎 " + user.getUsername());
            new MainFrame(user).setVisible(true);
            dispose();
        } else {
            attemptCount++;
            if (attemptCount >= 3) {
                JOptionPane.showMessageDialog(this, "错误次数过多，系统退出");
                System.exit(0);
            }
            JOptionPane.showMessageDialog(this, "用户名或密码错误！(第" + attemptCount + "次)");
        }
    }
}
