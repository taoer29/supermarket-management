package com.supermarket.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 文件工具类——HTML报表生成 & 数据备份
 */
public class FileUtil {

    /**
     * 生成商品库存HTML报表
     */
    public static String generateProductReportHtml(String[][] data, String[] headers) {
        StringBuilder html = new StringBuilder();
        html.append("""
            <!DOCTYPE html>
            <html><head><meta charset="UTF-8">
            <title>商品库存报表</title>
            <style>
                body { font-family: 'Microsoft YaHei', sans-serif; margin: 20px; }
                h1 { text-align: center; color: #333; }
                table { width: 100%; border-collapse: collapse; margin-top: 20px; }
                th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }
                th { background-color: #4CAF50; color: white; }
                tr:nth-child(even) { background-color: #f2f2f2; }
                .footer { text-align: center; margin-top: 20px; color: #666; }
            </style></head><body>
            <h1>商品库存报表</h1>
            <table><tr>
            """);
        for (String h : headers) html.append("<th>").append(h).append("</th>");
        html.append("</tr>");
        for (String[] row : data) {
            html.append("<tr>");
            for (String cell : row) html.append("<td>").append(cell).append("</td>");
            html.append("</tr>");
        }
        html.append("</table>");
        html.append("<div class='footer'>生成时间：").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("</div>");
        html.append("</body></html>");
        return html.toString();
    }

    /**
     * 生成销售记录HTML报表
     */
    public static String generateSaleReportHtml(String[][] data, String[] headers) {
        // 复用生成逻辑，可自定义不同样式
        return generateProductReportHtml(data, headers).replace("商品库存报表", "销售记录报表");
    }

    /**
     * 将HTML写入文件
     */
    public static String saveHtml(String content, String fileName) {
        File dir = new File("reports");
        if (!dir.exists()) dir.mkdirs();

        String path = "reports" + File.separator + fileName + ".html";
        try (FileWriter fw = new FileWriter(path, java.nio.charset.StandardCharsets.UTF_8)) {
            fw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
