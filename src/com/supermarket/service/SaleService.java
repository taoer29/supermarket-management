package com.supermarket.service;

import com.supermarket.dao.*;
import com.supermarket.dao.impl.*;
import com.supermarket.dao.impl.sql.*;
import com.supermarket.entity.*;
import com.supermarket.util.Config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 销售业务逻辑层——核心业务
 *
 * 结算流程：扣库存 → 生成销售记录 → 生成明细 → 清空购物车
 */
public class SaleService {
    private final SaleDao saleDao = Config.isSqlMode() ? new SaleDaoSqlImpl() : new SaleDaoFileImpl();
    private final ProductDao productDao = Config.isSqlMode() ? new ProductDaoSqlImpl() : new ProductDaoFileImpl();
    private final SaleItemDao saleItemDao = Config.isSqlMode() ? new SaleItemDaoSqlImpl() : new SaleItemDaoFileImpl();

    /**
     * 结算购物车
     * @param items    购物车中的商品列表
     * @param operatorId 操作员ID
     * @return 生成的销售记录ID，失败返回-1
     */
    public int checkout(List<SaleItem> items, int operatorId) {
        // 1. 检查库存
        for (SaleItem item : items) {
            Product p = productDao.findById(item.getProductId());
            if (p == null || p.getStock() < item.getQuantity()) {
                return -1; // 库存不足
            }
        }

        // 2. 创建销售记录
        Sale sale = new Sale();
        sale.setSaleTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        sale.setOperatorId(operatorId);

        BigDecimal total = BigDecimal.ZERO;
        for (SaleItem item : items) {
            total = total.add(item.getSubtotal());
        }
        sale.setTotalAmount(total);

        if (saleDao.add(sale)) {
            // 3. 保存销售明细 & 扣库存
            for (SaleItem item : items) {
                item.setSaleId(sale.getId());
                saleItemDao.add(item);
                productDao.updateStock(item.getProductId(), -item.getQuantity());

                // 更新销量
                Product p = productDao.findById(item.getProductId());
                if (p != null) {
                    p.setSalesVolume(p.getSalesVolume() + item.getQuantity());
                    productDao.update(p);
                }
            }
            sale.setItems(items);
            return sale.getId();
        }
        return -1;
    }

    public List<Sale> findAll() { return saleDao.findAll(); }
    public List<Sale> findByPage(int page, int pageSize) { return saleDao.findByPage(page, pageSize); }
    public List<Sale> findByDateRange(String start, String end) { return saleDao.findByDateRange(start, end); }
    public int getTotalCount() { return saleDao.getTotalCount(); }
}
