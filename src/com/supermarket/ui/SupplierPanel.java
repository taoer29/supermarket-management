package com.supermarket.ui;

import com.supermarket.entity.Supplier;
import com.supermarket.service.SupplierService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

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
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> showSupplierDialog(null));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "请先选择要修改的供应商"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            showSupplierDialog(supplierService.findById(id));
        });
        btnDelete.addActionListener(e -> doDelete());
        btnRefresh.addActionListener(e -> refreshTable());
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Supplier> all = supplierService.findAll();
        for (Supplier s : all) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(),
                    s.getPhone() == null ? "" : s.getPhone(),
                    s.getAddress() == null ? "" : s.getAddress()
            });
        }
    }

    // ==================== 新增/修改对话框 ====================

    private void showSupplierDialog(Supplier supplier) {
        boolean isNew = (supplier == null);

        JTextField nameField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        JTextField addressField = new JTextField(20);

        if (!isNew) {
            nameField.setText(supplier.getName());
            phoneField.setText(supplier.getPhone());
            addressField.setText(supplier.getAddress());
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("供应商名称："), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("联系电话："), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("地址："), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel,
                isNew ? "新增供应商" : "修改供应商", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        String name = nameField.getText().trim();
        if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "供应商名称不能为空"); return; }

        if (isNew) {
            Supplier s = new Supplier(name, phoneField.getText().trim(), addressField.getText().trim());
            if (supplierService.addSupplier(s)) {
                JOptionPane.showMessageDialog(this, "✅ 供应商添加成功");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "❌ 添加失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            supplier.setName(name);
            supplier.setPhone(phoneField.getText().trim());
            supplier.setAddress(addressField.getText().trim());
            if (supplierService.updateSupplier(supplier)) {
                JOptionPane.showMessageDialog(this, "✅ 供应商修改成功");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "❌ 修改失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== 删除 ====================

    private void doDelete() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "请先选择要删除的供应商"); return; }

        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "确认删除供应商「" + name + "」？", "删除确认", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (supplierService.deleteSupplier(id)) {
                JOptionPane.showMessageDialog(this, "✅ 已删除");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "❌ 删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
