package com.supermarket.ui;

import com.supermarket.entity.Product;
import com.supermarket.service.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * 商品管理面板——CRUD + 分页 + 搜索 + 库存预警
 *
 * TODO: 完善增删改查对话框、分页控制、分类筛选
 */
public class ProductPanel extends JPanel {
    private final ProductService productService = new ProductService();
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

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Product p : productService.findAll()) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getCategoryId(),
                    p.getStock(), p.getCostPrice(), p.getSellingPrice(),
                    p.getMinStock(), p.getStock() <= p.getMinStock() ? "⚠ 预警" : "正常"
            });
        }
    }
}
