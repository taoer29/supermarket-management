package com.supermarket.ui;

import com.supermarket.entity.Product;
import com.supermarket.entity.Purchase;
import com.supermarket.entity.Supplier;
import com.supermarket.entity.User;
import com.supermarket.service.ProductService;
import com.supermarket.service.PurchaseService;
import com.supermarket.service.SupplierService;
import com.supermarket.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * 进货管理面板——进货录入 + 记录查询
 */
public class PurchasePanel extends JPanel {
    private final PurchaseService purchaseService = new PurchaseService();
    private final ProductService productService = new ProductService();
    private final SupplierService supplierService = new SupplierService();
    private final UserService userService = new UserService();
    private final User currentUser;

    private JTable table;
    private DefaultTableModel tableModel;

    public PurchasePanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ========== 顶部工具栏 ==========
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton btnAdd = new JButton("新增进货");
        btnAdd.setBackground(new Color(70, 130, 180));
        btnAdd.setForeground(Color.WHITE);
        JButton btnRefresh = new JButton("刷新");

        // 筛选
        JTextField startDateField = new JTextField(10);
        JTextField endDateField = new JTextField(10);
        JButton btnFilterDate = new JButton("按日期筛选");
        JButton btnShowAll = new JButton("显示全部");

        toolBar.add(btnAdd);
        toolBar.add(btnRefresh);
        toolBar.add(new JLabel(" 日期从："));
        toolBar.add(startDateField);
        toolBar.add(new JLabel(" 到："));
        toolBar.add(endDateField);
        toolBar.add(btnFilterDate);
        toolBar.add(btnShowAll);
        add(toolBar, BorderLayout.NORTH);

        // ========== 表格 ==========
        String[] columns = {"单号", "商品名", "供应商", "数量", "单价", "总金额", "进货日期", "操作员"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 底部状态
        JLabel statusLabel = new JLabel(" 共 0 条记录");
        add(statusLabel, BorderLayout.SOUTH);

        // ========== 事件绑定 ==========
        btnAdd.addActionListener(e -> showPurchaseDialog());
        btnRefresh.addActionListener(e -> refreshTable());
        btnFilterDate.addActionListener(e -> {
            String start = startDateField.getText().trim();
            String end = endDateField.getText().trim();
            if (start.isEmpty() || end.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写起止日期，格式：yyyy-MM-dd");
                return;
            }
            // 补全时间范围
            String startFull = start + " 00:00:00";
            String endFull = end + " 23:59:59";
            List<Purchase> list = purchaseService.findByDateRange(startFull, endFull);
            refreshTable(list);
            statusLabel.setText(" 共 " + list.size() + " 条记录（日期范围：" + start + " ~ " + end + "）");
        });
        btnShowAll.addActionListener(e -> {
            startDateField.setText("");
            endDateField.setText("");
            refreshTable();
            statusLabel.setText(" 共 " + purchaseService.getTotalCount() + " 条记录");
        });

        refreshTable();
    }

    // ==================== 刷新表格 ====================

    private void refreshTable() {
        refreshTable(purchaseService.findAll());
    }

    private void refreshTable(List<Purchase> list) {
        // 构建名称映射
        Map<Integer, String> productMap = new HashMap<>();
        for (Product p : productService.findAll()) {
            productMap.put(p.getId(), p.getName());
        }
        Map<Integer, String> supplierMap = new HashMap<>();
        for (Supplier s : supplierService.findAll()) {
            supplierMap.put(s.getId(), s.getName());
        }
        Map<Integer, String> userMap = new HashMap<>();
        for (User u : userService.findAll()) {
            userMap.put(u.getId(), u.getUsername());
        }

        tableModel.setRowCount(0);
        for (Purchase p : list) {
            String productName = productMap.getOrDefault(p.getProductId(), "未知商品(ID:" + p.getProductId() + ")");
            String supplierName = supplierMap.getOrDefault(p.getSupplierId(), "未知供应商(ID:" + p.getSupplierId() + ")");
            String operatorName = userMap.getOrDefault(p.getOperatorId(), "未知");
            tableModel.addRow(new Object[]{
                    p.getId(), productName, supplierName,
                    p.getQuantity(), p.getUnitPrice(), p.getTotalPrice(),
                    p.getPurchaseDate(), operatorName
            });
        }
    }

    // ==================== 新增进货对话框 ====================

    private void showPurchaseDialog() {
        // 获取商品和供应商列表
        List<Product> products = productService.findAll();
        List<Supplier> suppliers = supplierService.findAll();

        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先在商品管理中录入商品");
            return;
        }
        if (suppliers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先在供应商管理中录入供应商");
            return;
        }

        JComboBox<String> productCombo = new JComboBox<>();
        for (Product p : products) {
            productCombo.addItem(p.getId() + " - " + p.getName() + "（库存:" + p.getStock() + "）");
        }

        JComboBox<String> supplierCombo = new JComboBox<>();
        for (Supplier s : suppliers) {
            supplierCombo.addItem(s.getId() + " - " + s.getName());
        }

        JTextField quantityField = new JTextField(8);
        JTextField priceField = new JTextField(8);

        // 选中商品时自动填入进价
        productCombo.addActionListener(e -> {
            int idx = productCombo.getSelectedIndex();
            if (idx >= 0) {
                priceField.setText(products.get(idx).getCostPrice().toString());
            }
        });
        // 默认选中第一个时触发
        if (!products.isEmpty()) {
            priceField.setText(products.get(0).getCostPrice().toString());
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("商品："), gbc);
        gbc.gridx = 1;
        productCombo.setPreferredSize(new Dimension(250, 25));
        panel.add(productCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("供应商："), gbc);
        gbc.gridx = 1;
        supplierCombo.setPreferredSize(new Dimension(250, 25));
        panel.add(supplierCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("进货数量："), gbc);
        gbc.gridx = 1;
        panel.add(quantityField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("进货单价："), gbc);
        gbc.gridx = 1;
        panel.add(priceField, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "新增进货记录", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        int productIdx = productCombo.getSelectedIndex();
        int supplierIdx = supplierCombo.getSelectedIndex();
        if (productIdx < 0 || supplierIdx < 0) return;

        int quantity;
        BigDecimal unitPrice;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
            unitPrice = new BigDecimal(priceField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "数量和单价必须为有效数字");
            return;
        }

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "进货数量必须大于0");
            return;
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this, "单价必须大于0");
            return;
        }

        Purchase purchase = new Purchase();
        purchase.setProductId(products.get(productIdx).getId());
        purchase.setSupplierId(suppliers.get(supplierIdx).getId());
        purchase.setQuantity(quantity);
        purchase.setUnitPrice(unitPrice);
        purchase.setPurchaseDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        purchase.setOperatorId(currentUser.getId());

        if (purchaseService.addPurchase(purchase)) {
            JOptionPane.showMessageDialog(this, "✅ 进货成功！库存已更新");
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "❌ 进货失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
