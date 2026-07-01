package com.supermarket.ui;

import com.supermarket.service.FileService;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 报表统计面板——生成HTML报表 & 数据备份
 */
public class ReportPanel extends JPanel {
    private final FileService fileService = new FileService();
    private JTextArea logArea;

    public ReportPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 功能按钮
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton btnProductReport = new JButton("生成商品库存报表");
        JButton btnSaleReport = new JButton("生成销售记录报表");
        JButton btnOpenReports = new JButton("打开报表目录");

        btnProductReport.setPreferredSize(new Dimension(160, 40));
        btnSaleReport.setPreferredSize(new Dimension(160, 40));
        btnOpenReports.setPreferredSize(new Dimension(160, 40));

        btnPanel.add(btnProductReport);
        btnPanel.add(btnSaleReport);
        btnPanel.add(btnOpenReports);
        add(btnPanel, BorderLayout.NORTH);

        // 日志区域
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        // 事件
        btnProductReport.addActionListener(e -> {
            String path = fileService.exportProductReport();
            log("商品库存报表已生成：" + path);
        });

        btnSaleReport.addActionListener(e -> {
            String path = fileService.exportSaleReport();
            log("销售记录报表已生成：" + path);
        });

        btnOpenReports.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new File("reports"));
            } catch (Exception ex) {
                log("无法打开目录：" + ex.getMessage());
            }
        });
    }

    private void log(String msg) {
        logArea.append(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))
                + " - " + msg + "\n");
    }
}
