package com.supermarket.ui;

import com.supermarket.entity.*;
import com.supermarket.service.ProductService;
import com.supermarket.service.SaleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;

/**
 * 收银台面板——购物车 + 结算
 *
 * TODO: 完善商品搜索添加、数量修改、找零计算
 */
public class SalePanel extends JPanel {
    private final ProductService productService = new ProductService();
    private final SaleService saleService = new SaleService();
    private final User currentUser;
    private DefaultTableModel cartTableModel;
    private JLabel totalLabel;

    public SalePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 顶部：商品搜索
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton btnSearch = new JButton("搜索商品");
        topPanel.add(new JLabel("商品搜索："));
        topPanel.add(searchField);
        topPanel.add(btnSearch);
        add(topPanel, BorderLayout.NORTH);

        // 中间：购物车表格
        String[] columns = {"商品编号", "商品名", "单价", "数量", "小计"};
        cartTableModel = new DefaultTableModel(columns, 0);
        JTable cartTable = new JTable(cartTableModel);
        add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // 底部：结算区
        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("总计：¥0.00");
        totalLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        totalLabel.setForeground(new Color(200, 50, 50));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAddItem = new JButton("添加商品到购物车");
        JButton btnClear = new JButton("清空购物车");
        JButton btnCheckout = new JButton("结算");
        btnCheckout.setBackground(new Color(50, 180, 50));
        btnCheckout.setForeground(Color.WHITE);

        btnPanel.add(btnAddItem);
        btnPanel.add(btnClear);
        btnPanel.add(btnCheckout);

        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(btnPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // 事件
        btnCheckout.addActionListener(e -> doCheckout());
        btnClear.addActionListener(e -> cartTableModel.setRowCount(0));
    }

    private void doCheckout() {
        // TODO: 将购物车数据转为 SaleItem 列表
        // int saleId = saleService.checkout(items, currentUser.getId());
        JOptionPane.showMessageDialog(this, "结算功能待完善");
    }
}
