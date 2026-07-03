package com.supermarket.ui;

import com.supermarket.dao.CategoryDao;
import com.supermarket.dao.impl.CategoryDaoFileImpl;
import com.supermarket.dao.impl.sql.CategoryDaoSqlImpl;
import com.supermarket.entity.Category;
import com.supermarket.service.ProductService;
import com.supermarket.util.Config;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 分类管理面板——管理商品分类
 */
public class CategoryPanel extends JPanel {
    private final CategoryDao categoryDao = Config.isSqlMode() ? new CategoryDaoSqlImpl() : new CategoryDaoFileImpl();
    private final ProductService productService = new ProductService();
    private JTable table;
    private DefaultTableModel tableModel;

    public CategoryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 工具栏
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("新增分类");
        JButton btnEdit = new JButton("重命名");
        JButton btnDelete = new JButton("删除");
        JButton btnRefresh = new JButton("刷新");

        toolBar.add(btnAdd);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);
        toolBar.add(btnRefresh);
        add(toolBar, BorderLayout.NORTH);

        // 表格
        String[] columns = {"编号", "分类名称"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 事件
        btnAdd.addActionListener(e -> doAdd());
        btnEdit.addActionListener(e -> doEdit());
        btnDelete.addActionListener(e -> doDelete());
        btnRefresh.addActionListener(e -> refreshTable());

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Category c : categoryDao.findAll()) {
            tableModel.addRow(new Object[]{c.getId(), c.getName()});
        }
    }

    private void doAdd() {
        String name = JOptionPane.showInputDialog(this, "请输入分类名称：", "新增分类", JOptionPane.PLAIN_MESSAGE);
        if (name == null) return;
        name = name.trim();
        if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "分类名称不能为空"); return; }
        if (categoryDao.findByName(name) != null) { JOptionPane.showMessageDialog(this, "分类「" + name + "」已存在"); return; }

        categoryDao.add(new Category(name));
        JOptionPane.showMessageDialog(this, "✅ 分类添加成功");
        refreshTable();
    }

    private void doEdit() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "请先选择要重命名的分类"); return; }

        Category c = categoryDao.findById((int) tableModel.getValueAt(row, 0));
        String name = JOptionPane.showInputDialog(this, "修改分类名称：", c.getName());
        if (name == null) return;
        name = name.trim();
        if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "分类名称不能为空"); return; }
        if (!name.equals(c.getName()) && categoryDao.findByName(name) != null) {
            JOptionPane.showMessageDialog(this, "分类「" + name + "」已存在"); return;
        }

        c.setName(name);
        categoryDao.update(c);
        JOptionPane.showMessageDialog(this, "✅ 已重命名");
        refreshTable();
    }

    private void doDelete() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "请先选择要删除的分类"); return; }

        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        // 检查是否有商品在使用这个分类
        boolean inUse = productService.findAll().stream()
                .anyMatch(p -> p.getCategoryId() == id);
        if (inUse) {
            JOptionPane.showMessageDialog(this,
                    "❌ 无法删除：分类「" + name + "」下还有商品在使用\n请先将相关商品移出此分类",
                    "删除失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "确认删除分类「" + name + "」？", "删除确认", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            categoryDao.delete(id);
            JOptionPane.showMessageDialog(this, "✅ 已删除");
            refreshTable();
        }
    }
}
