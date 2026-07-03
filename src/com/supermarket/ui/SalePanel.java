package com.supermarket.ui;

import com.supermarket.entity.*;
import com.supermarket.service.ProductService;
import com.supermarket.service.SaleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 收银台面板——商品搜索 + 购物车 + 结算找零
 */
public class SalePanel extends JPanel {
    private final ProductService productService = new ProductService();
    private final SaleService saleService = new SaleService();
    private final User currentUser;

    // 搜索结果相关
    private DefaultTableModel searchTableModel;
    private JTable searchTable;

    // 购物车相关
    private DefaultTableModel cartTableModel;
    private JTable cartTable;
    private JLabel totalLabel;

    // 购物车数据（行索引 → SaleItem）
    private final List<SaleItem> cartItems = new ArrayList<>();

    public SalePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ========== 顶部：搜索栏 ==========
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JTextField searchField = new JTextField(20);
        JButton btnSearch = new JButton("搜索商品");
        JButton btnAddToCart = new JButton("加入购物车");
        btnAddToCart.setBackground(new Color(60, 179, 113));
        btnAddToCart.setForeground(Color.WHITE);
        topPanel.add(new JLabel("🔍 商品搜索："));
        topPanel.add(searchField);
        topPanel.add(btnSearch);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(btnAddToCart);
        add(topPanel, BorderLayout.NORTH);

        // ========== 中间：左右分割 ==========
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.45);
        splitPane.setDividerSize(5);

        // ---- 左侧：搜索结果 ----
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("搜索结果"));
        String[] searchCols = {"编号", "商品名", "售价", "库存"};
        searchTableModel = new DefaultTableModel(searchCols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        searchTable = new JTable(searchTableModel);
        searchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchTable.getTableHeader().setReorderingAllowed(false);
        leftPanel.add(new JScrollPane(searchTable), BorderLayout.CENTER);
        splitPane.setLeftComponent(leftPanel);

        // ---- 右侧：购物车 ----
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder("购物车"));
        String[] cartCols = {"编号", "商品名", "单价", "数量", "小计"};
        cartTableModel = new DefaultTableModel(cartCols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        cartTable = new JTable(cartTableModel);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartTable.getTableHeader().setReorderingAllowed(false);
        rightPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // 购物车操作按钮
        JPanel cartBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnModify = new JButton("修改数量");
        JButton btnRemove = new JButton("移除");
        JButton btnClear = new JButton("清空购物车");
        cartBtnPanel.add(btnModify);
        cartBtnPanel.add(btnRemove);
        cartBtnPanel.add(btnClear);
        rightPanel.add(cartBtnPanel, BorderLayout.SOUTH);
        splitPane.setRightComponent(rightPanel);

        add(splitPane, BorderLayout.CENTER);

        // ========== 底部：总计 + 结算 ==========
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 5, 5, 5));

        totalLabel = new JLabel("总计：¥0.00");
        totalLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        totalLabel.setForeground(new Color(200, 50, 50));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnCheckout = new JButton("💳 结算");
        btnCheckout.setFont(new Font("微软雅黑", Font.BOLD, 16));
        btnCheckout.setPreferredSize(new Dimension(140, 40));
        btnCheckout.setBackground(new Color(50, 180, 50));
        btnCheckout.setForeground(Color.WHITE);

        actionPanel.add(btnCheckout);
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(actionPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // ========== 事件绑定 ==========
        // 搜索
        btnSearch.addActionListener(e -> doSearch(searchField.getText().trim()));
        searchField.addActionListener(e -> doSearch(searchField.getText().trim()));

        // 添加到购物车
        btnAddToCart.addActionListener(e -> addSelectedToCart());

        // 双击搜索结果快速加入购物车
        searchTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addSelectedToCart();
                }
            }
        });

        // 修改数量
        btnModify.addActionListener(e -> modifyCartQuantity());

        // 移除
        btnRemove.addActionListener(e -> removeFromCart());

        // 清空
        btnClear.addActionListener(e -> clearCart());

        // 结算
        btnCheckout.addActionListener(e -> doCheckout());
    }

    // ==================== 搜索 ====================

    private void doSearch(String keyword) {
        searchTableModel.setRowCount(0);
        if (keyword.isEmpty()) {
            // 搜索全部
            for (Product p : productService.findAll()) {
                searchTableModel.addRow(new Object[]{p.getId(), p.getName(), p.getSellingPrice(), p.getStock()});
            }
        } else {
            for (Product p : productService.searchByName(keyword)) {
                searchTableModel.addRow(new Object[]{p.getId(), p.getName(), p.getSellingPrice(), p.getStock()});
            }
        }
    }

    // ==================== 购物车操作 ====================

    /**
     * 将选中搜索结果加入购物车
     */
    private void addSelectedToCart() {
        int row = searchTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先在搜索结果中选择商品");
            return;
        }

        int productId = (int) searchTableModel.getValueAt(row, 0);
        String name = (String) searchTableModel.getValueAt(row, 1);
        BigDecimal price = (BigDecimal) searchTableModel.getValueAt(row, 2);
        int stock = (int) searchTableModel.getValueAt(row, 3);

        if (stock <= 0) {
            JOptionPane.showMessageDialog(this, "该商品库存不足，无法添加");
            return;
        }

        // 询问数量
        String qtyStr = JOptionPane.showInputDialog(this,
                "商品：" + name + "\n售价：¥" + price + "\n库存：" + stock + "\n请输入数量：",
                "加入购物车", JOptionPane.PLAIN_MESSAGE);
        if (qtyStr == null) return; // 取消

        int qty;
        try {
            qty = Integer.parseInt(qtyStr.trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的正整数数量");
            return;
        }

        if (qty > stock) {
            JOptionPane.showMessageDialog(this, "库存不足！当前库存：" + stock);
            return;
        }

        // 检查购物车是否已有该商品
        for (SaleItem item : cartItems) {
            if (item.getProductId() == productId) {
                int newQty = item.getQuantity() + qty;
                if (newQty > stock) {
                    JOptionPane.showMessageDialog(this, "购物车中已有 " + item.getQuantity()
                            + " 件，加上本次最多还能添加 " + (stock - item.getQuantity()) + " 件");
                    return;
                }
                item.setQuantity(newQty);
                item.setSubtotal(price.multiply(BigDecimal.valueOf(newQty)));
                refreshCartTable();
                return;
            }
        }

        // 新增购物车项
        SaleItem item = new SaleItem(productId, name, qty, price);
        cartItems.add(item);
        refreshCartTable();
    }

    /**
     * 修改购物车中某商品的数量
     */
    private void modifyCartQuantity() {
        int row = cartTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先在购物车中选择要修改的商品");
            return;
        }

        SaleItem item = cartItems.get(row);
        Product product = productService.findById(item.getProductId());
        int maxStock = (product != null) ? product.getStock() + item.getQuantity() : Integer.MAX_VALUE;

        String qtyStr = JOptionPane.showInputDialog(this,
                "商品：" + item.getProductName() + "\n当前数量：" + item.getQuantity()
                        + "\n可用库存：" + maxStock + "\n修改为：",
                "修改数量", JOptionPane.PLAIN_MESSAGE);
        if (qtyStr == null) return;

        int qty;
        try {
            qty = Integer.parseInt(qtyStr.trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的正整数");
            return;
        }

        if (qty > maxStock) {
            JOptionPane.showMessageDialog(this, "可用库存不足！最大可设置为：" + maxStock);
            return;
        }

        BigDecimal unitPrice = item.getSubtotal().divide(BigDecimal.valueOf(item.getQuantity()),
                2, java.math.RoundingMode.HALF_UP);
        item.setQuantity(qty);
        item.setSubtotal(unitPrice.multiply(BigDecimal.valueOf(qty)));
        refreshCartTable();
    }

    /**
     * 从购物车移除选中商品
     */
    private void removeFromCart() {
        int row = cartTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先在购物车中选择要移除的商品");
            return;
        }
        cartItems.remove(row);
        refreshCartTable();
    }

    /**
     * 清空购物车
     */
    private void clearCart() {
        if (cartItems.isEmpty()) return;
        int confirm = JOptionPane.showConfirmDialog(this, "确认清空购物车？",
                "清空购物车", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            cartItems.clear();
            refreshCartTable();
        }
    }

    // ==================== 刷新购物车表格 ====================

    private void refreshCartTable() {
        cartTableModel.setRowCount(0);
        BigDecimal total = BigDecimal.ZERO;
        for (SaleItem item : cartItems) {
            cartTableModel.addRow(new Object[]{
                    item.getProductId(), item.getProductName(),
                    item.getSubtotal().divide(BigDecimal.valueOf(item.getQuantity()),
                            2, java.math.RoundingMode.HALF_UP),
                    item.getQuantity(), item.getSubtotal()
            });
            total = total.add(item.getSubtotal());
        }
        totalLabel.setText("总计：¥" + total.setScale(2, java.math.RoundingMode.HALF_UP));
    }

    // ==================== 结算 & 找零 ====================

    private void doCheckout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "购物车为空，请先添加商品");
            return;
        }

        // 确认结算
        BigDecimal total = BigDecimal.ZERO;
        for (SaleItem item : cartItems) {
            total = total.add(item.getSubtotal());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("===== 结算确认 =====\n\n");
        for (SaleItem item : cartItems) {
            sb.append(item.getProductName()).append(" × ").append(item.getQuantity())
                    .append("  =  ¥").append(item.getSubtotal()).append("\n");
        }
        sb.append("\n应付金额：¥").append(total.setScale(2, java.math.RoundingMode.HALF_UP));

        int confirm = JOptionPane.showConfirmDialog(this, sb.toString(),
                "结算确认", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        // 调用结算
        List<SaleItem> checkoutItems = new ArrayList<>(cartItems);
        int saleId = saleService.checkout(checkoutItems, currentUser.getId());

        if (saleId < 0) {
            JOptionPane.showMessageDialog(this, "❌ 结算失败：部分商品库存不足，请检查后重试",
                    "结算失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 结算成功 → 找零界面
        showPaymentDialog(total, saleId);

        // 清空购物车
        cartItems.clear();
        refreshCartTable();
    }

    /**
     * 显示收款找零对话框
     */
    private void showPaymentDialog(BigDecimal total, int saleId) {
        JTextField paymentField = new JTextField(12);
        paymentField.setFont(new Font("微软雅黑", Font.PLAIN, 16));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("应收金额："), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel("¥" + total.setScale(2, java.math.RoundingMode.HALF_UP)), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("实收金额："), gbc);
        gbc.gridx = 1;
        panel.add(paymentField, gbc);

        JLabel changeLabel = new JLabel("找零：");
        changeLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("找零："), gbc);
        gbc.gridx = 1;
        panel.add(changeLabel, gbc);

        // 实时计算找零
        paymentField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            void calc() {
                try {
                    BigDecimal paid = new BigDecimal(paymentField.getText().trim());
                    if (paid.compareTo(total) >= 0) {
                        changeLabel.setText("¥" + paid.subtract(total).setScale(2, java.math.RoundingMode.HALF_UP));
                        changeLabel.setForeground(new Color(0, 128, 0));
                    } else {
                        changeLabel.setText("¥0.00（不足）");
                        changeLabel.setForeground(Color.RED);
                    }
                } catch (Exception e) {
                    changeLabel.setText("¥0.00");
                    changeLabel.setForeground(Color.GRAY);
                }
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { calc(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { calc(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { calc(); }
        });

        int option = JOptionPane.showConfirmDialog(this, panel, "收款 - 单号 #" + saleId,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                BigDecimal paid = new BigDecimal(paymentField.getText().trim());
                if (paid.compareTo(total) < 0) {
                    JOptionPane.showMessageDialog(this, "实收金额不足，请重新收款", "提示", JOptionPane.WARNING_MESSAGE);
                    showPaymentDialog(total, saleId); // 重新输入
                    return;
                }
                BigDecimal change = paid.subtract(total);
                JOptionPane.showMessageDialog(this,
                        "✅ 结算成功！单号：#" + saleId + "\n"
                                + "应收：¥" + total.setScale(2, java.math.RoundingMode.HALF_UP) + "\n"
                                + "实收：¥" + paid.setScale(2, java.math.RoundingMode.HALF_UP) + "\n"
                                + "找零：¥" + change.setScale(2, java.math.RoundingMode.HALF_UP),
                        "结算完成", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "请输入有效的金额", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
