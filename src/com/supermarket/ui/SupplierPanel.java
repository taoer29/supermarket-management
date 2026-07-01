package com.supermarket.ui;

import com.supermarket.entity.Supplier;
import com.supermarket.service.SupplierService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * 供应商管理面板
 */
public class SupplierPanel extends JPanel {
    private final SupplierService supplierService = new SupplierService();
    private JTable table;
    private DefaultTableModel tableModel;

    public SupplierPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("新增供应商");
        JButton btnEdit = new JButton("修改");
        JButton btnDelete = new JButton("删除");
        JButton btnRefresh = new JButton("刷新");

        toolBar.add(btnAdd);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);
        toolBar.add(btnRefresh);
        add(toolBar, BorderLayout.NORTH);

        String[] columns = {"编号", "名称", "联系电话", "地址"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> refreshTable());
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Supplier s : supplierService.findAll()) {
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getPhone(), s.getAddress()});
        }
    }
}
