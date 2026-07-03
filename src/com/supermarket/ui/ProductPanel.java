package com.supermarket.ui;

import com.supermarket.dao.CategoryDao;
import com.supermarket.dao.impl.CategoryDaoFileImpl;
import com.supermarket.dao.impl.sql.CategoryDaoSqlImpl;
import com.supermarket.entity.Category;
import com.supermarket.entity.Product;
import com.supermarket.service.ProductService;
import com.supermarket.util.Config;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品管理面板——CRUD + 搜索 + 库存预警
 *
 * TODO: 分页控制、按分类筛选
 */
public class ProductPanel extends JPanel {
    private final ProductService productService = new ProductService();
    private final CategoryDao categoryDao = Config.isSqlMode() ? new CategoryDaoSqlImpl() : new CategoryDaoFileImpl();
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public ProductPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 顶部工具栏
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("新增商品");
        JButton btnEdit = new JButton("修改商品");
        JButton btnDelete = new JButton("删除商品");
        JButton btnRefresh = new JButton("刷新");
        JTextField searchField = new JTextField(15);
        JButton btnSearch = new JButton("搜索");

        toolBar.add(btnAdd);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);
        toolBar.add(btnRefresh);
        toolBar.add(new JLabel(" 关键字："));
        toolBar.add(searchField);
        toolBar.add(btnSearch);
        add(toolBar, BorderLayout.NORTH);

        // 表格
        String[] columns = {"编号", "商品名", "分类", "库存", "进价", "售价", "最低库存", "状态"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 底部状态
        statusLabel = new JLabel(" 共 0 条记录");
        add(statusLabel, BorderLayout.SOUTH);

        // 事件绑定
        btnAdd.addActionListener(e -> showProductDialog(null));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请先选择要修改的商品");
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            Product p = productService.findById(id);
            if (p != null) showProductDialog(p);
        });
        btnDelete.addActionListener(e -> doDelete());
        btnRefresh.addActionListener(e -> refreshTable());
        btnSearch.addActionListener(e -> doSearch(searchField.getText().trim()));
        searchField.addActionListener(e -> doSearch(searchField.getText().trim()));

        // 加载数据
        refreshTable();
    }

    // ==================== 分类映射 ====================

    private Map<Integer, String> buildCategoryMap() {
        Map<Integer, String> map = new HashMap<>();
        for (Category c : categoryDao.findAll()) {
            map.put(c.getId(), c.getName());
        }
        return map;
    }

    /**
     * 获取分类ID → JComboBox 选择索引 的映射（用于对话框预选）
     */
    private Map<Integer, Integer> buildCategoryIndexMap(List<Category> cats) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < cats.size(); i++) {
            map.put(cats.get(i).getId(), i);
        }
        return map;
    }

    // ==================== 刷新表格 ====================

    private void refreshTable() {
        Map<Integer, String> categoryMap = buildCategoryMap();
        tableModel.setRowCount(0);
        List<Product> all = productService.findAll();
        for (Product p : all) {
            String categoryName = categoryMap.getOrDefault(p.getCategoryId(), "未分类(ID:" + p.getCategoryId() + ")");
            tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), categoryName,
                    p.getStock(), p.getCostPrice(), p.getSellingPrice(),
                    p.getMinStock(), p.getStock() <= p.getMinStock() ? "⚠ 预警" : "正常"
            });
        }
        statusLabel.setText(" 共 " + all.size() + " 条记录");
    }

    // ==================== 搜索 ====================

    private void doSearch(String keyword) {
        tableModel.setRowCount(0);
        Map<Integer, String> categoryMap = buildCategoryMap();
        List<Product> list;
        if (keyword.isEmpty()) {
            list = productService.findAll();
        } else {
            list = productService.searchByName(keyword);
        }
        for (Product p : list) {
            String categoryName = categoryMap.getOrDefault(p.getCategoryId(), "未分类(ID:" + p.getCategoryId() + ")");
            tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), categoryName,
                    p.getStock(), p.getCostPrice(), p.getSellingPrice(),
                    p.getMinStock(), p.getStock() <= p.getMinStock() ? "⚠ 预警" : "正常"
            });
        }
        statusLabel.setText(" 共 " + list.size() + " 条记录（搜索：" + (keyword.isEmpty() ? "全部" : keyword) + "）");
    }

    // ==================== 新增/修改对话框 ====================

    /**
     * 弹出商品编辑对话框
     * @param product null表示新增，非null表示修改
     */
    private void showProductDialog(Product product) {
        boolean isNew = (product == null);

        JTextField nameField = new JTextField(15);
        JTextField costField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JTextField stockField = new JTextField(10);
        JTextField minStockField = new JTextField(10);
        JComboBox<String> categoryCombo = new JComboBox<>();
        List<Category> cats = categoryDao.findAll();
        for (Category c : cats) {
            categoryCombo.addItem(c.getName());
        }

        // 修改模式下预填数据
        if (!isNew) {
            nameField.setText(product.getName());
            costField.setText(product.getCostPrice().toString());
            priceField.setText(product.getSellingPrice().toString());
            stockField.setText(String.valueOf(product.getStock()));
            minStockField.setText(String.valueOf(product.getMinStock()));
            // 预选分类
            Map<Integer, Integer> indexMap = buildCategoryIndexMap(cats);
            Integer idx = indexMap.get(product.getCategoryId());
            if (idx != null) categoryCombo.setSelectedIndex(idx);
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        panel.add(new JLabel("商品名："), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("分类："), gbc);
        gbc.gridx = 1;
        panel.add(categoryCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("进价："), gbc);
        gbc.gridx = 1;
        panel.add(costField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("售价："), gbc);
        gbc.gridx = 1;
        panel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("库存："), gbc);
        gbc.gridx = 1;
        panel.add(stockField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("最低库存预警："), gbc);
        gbc.gridx = 1;
        panel.add(minStockField, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel,
                isNew ? "新增商品" : "修改商品", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        // === 校验输入 ===
        String name = nameField.getText().trim();
        if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "商品名不能为空"); return; }

        BigDecimal costPrice, sellingPrice;
        int stock, minStock;
        try {
            costPrice = new BigDecimal(costField.getText().trim());
            sellingPrice = new BigDecimal(priceField.getText().trim());
            stock = Integer.parseInt(stockField.getText().trim());
            minStock = Integer.parseInt(minStockField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "进价、售价、库存、最低库存必须为有效数字");
            return;
        }

        if (costPrice.compareTo(BigDecimal.ZERO) <= 0 || sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this, "价格必须大于0");
            return;
        }
        if (stock < 0 || minStock < 0) {
            JOptionPane.showMessageDialog(this, "库存不能为负数");
            return;
        }

        int categoryId = cats.get(categoryCombo.getSelectedIndex()).getId();

        if (isNew) {
            // 新增
            Product p = new Product();
            p.setName(name);
            p.setCategoryId(categoryId);
            p.setCostPrice(costPrice);
            p.setSellingPrice(sellingPrice);
            p.setStock(stock);
            p.setMinStock(minStock);
            p.setIsDeleted(0);

            if (productService.addProduct(p)) {
                JOptionPane.showMessageDialog(this, "✅ 商品添加成功");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "❌ 添加失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // 修改
            product.setName(name);
            product.setCategoryId(categoryId);
            product.setCostPrice(costPrice);
            product.setSellingPrice(sellingPrice);
            product.setStock(stock);
            product.setMinStock(minStock);

            if (productService.updateProduct(product)) {
                JOptionPane.showMessageDialog(this, "✅ 商品修改成功");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "❌ 修改失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== 删除 ====================

    private void doDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的商品");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "确认删除商品「" + name + "」？\n（将移入回收站，可恢复）",
                "删除确认", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (productService.deleteProduct(id)) {
                JOptionPane.showMessageDialog(this, "✅ 已删除（可恢复）");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "❌ 删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
