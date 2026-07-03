package com.supermarket.ui;

import com.supermarket.dao.CategoryDao;
import com.supermarket.dao.impl.CategoryDaoFileImpl;
import com.supermarket.entity.Category;
import com.supermarket.entity.Product;
import com.supermarket.service.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品管理面板——CRUD + 分页 + 搜索 + 库存预警
 *
 * TODO: 完善增删改查对话框、分页控制、分类筛选
 */
public class ProductPanel extends JPanel {
    private final ProductService productService = new ProductService();
    private final CategoryDao categoryDao = new CategoryDaoFileImpl();
    private JTable table;
    private DefaultTableModel tableModel;

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
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 底部状态
        JLabel statusLabel = new JLabel(" 共 0 条记录");
        add(statusLabel, BorderLayout.SOUTH);

        // 事件绑定
        btnRefresh.addActionListener(e -> refreshTable());

        // 加载数据
        refreshTable();
    }

    /**
     * 构建分类ID → 分类名称 的映射
     */
    private Map<Integer, String> buildCategoryMap() {
        Map<Integer, String> map = new HashMap<>();
        for (Category c : categoryDao.findAll()) {
            map.put(c.getId(), c.getName());
        }
        return map;
    }

    private void refreshTable() {
        Map<Integer, String> categoryMap = buildCategoryMap();
        tableModel.setRowCount(0);
        for (Product p : productService.findAll()) {
            String categoryName = categoryMap.getOrDefault(p.getCategoryId(), "未分类(ID:" + p.getCategoryId() + ")");
            tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), categoryName,
                    p.getStock(), p.getCostPrice(), p.getSellingPrice(),
                    p.getMinStock(), p.getStock() <= p.getMinStock() ? "⚠ 预警" : "正常"
            });
        }
    }
}
