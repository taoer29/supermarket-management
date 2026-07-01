package com.supermarket.service;

import com.supermarket.util.FileUtil;

/**
 * 文件服务——报表生成 & 数据备份
 */
public class FileService {

    private final ProductService productService = new ProductService();
    private final SaleService saleService = new SaleService();

    /**
     * 生成商品库存HTML报表
     */
    public String exportProductReport() {
        var products = productService.findAll();
        String[][] data = new String[products.size()][6];
        for (int i = 0; i < products.size(); i++) {
            var p = products.get(i);
            data[i][0] = String.valueOf(p.getId());
            data[i][1] = p.getName();
            data[i][2] = String.valueOf(p.getStock());
            data[i][3] = String.valueOf(p.getSellingPrice());
            data[i][4] = String.valueOf(p.getMinStock());
            data[i][5] = p.getStock() <= p.getMinStock() ? "⚠ 预警" : "正常";
        }
        String[] headers = {"编号", "商品名", "库存", "售价", "最低库存", "状态"};
        String html = FileUtil.generateProductReportHtml(data, headers);
        return FileUtil.saveHtml(html, "product_report_" + java.time.LocalDate.now());
    }

    /**
     * 生成销售记录HTML报表
     */
    public String exportSaleReport() {
        var sales = saleService.findAll();
        String[][] data = new String[sales.size()][5];
        for (int i = 0; i < sales.size(); i++) {
            var s = sales.get(i);
            data[i][0] = String.valueOf(s.getId());
            data[i][1] = String.valueOf(s.getTotalAmount());
            data[i][2] = s.getSaleTime();
            data[i][3] = String.valueOf(s.getItems().size() + "项");
            data[i][4] = String.valueOf(s.getOperatorId());
        }
        String[] headers = {"单号", "总金额", "销售时间", "商品数", "操作员"};
        String html = FileUtil.generateSaleReportHtml(data, headers);
        return FileUtil.saveHtml(html, "sale_report_" + java.time.LocalDate.now());
    }
}
