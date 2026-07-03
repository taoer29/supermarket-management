package com.supermarket.service;

import com.supermarket.dao.PurchaseDao;
import com.supermarket.dao.impl.PurchaseDaoFileImpl;
import com.supermarket.entity.Purchase;
import com.supermarket.entity.Product;
import com.supermarket.service.ProductService;

import java.util.List;

/**
 * 进货业务逻辑层
 *
 * 进货流程：创建进货记录 → 更新对应商品的库存（增加）
 */
public class PurchaseService {
    private final PurchaseDao purchaseDao = new PurchaseDaoFileImpl();
    private final ProductService productService = new ProductService();

    /**
     * 新增进货记录并更新库存
     * @return true 成功，false 失败
     */
    public boolean addPurchase(Purchase purchase) {
        // 1. 计算总金额
        purchase.setTotalPrice(
                purchase.getUnitPrice().multiply(java.math.BigDecimal.valueOf(purchase.getQuantity()))
        );

        // 2. 保存进货记录
        if (!purchaseDao.add(purchase)) {
            return false;
        }

        // 3. 更新商品库存
        return productService.updateStock(purchase.getProductId(), purchase.getQuantity());
    }

    public List<Purchase> findAll() { return purchaseDao.findAll(); }

    public List<Purchase> findByPage(int page, int pageSize) {
        return purchaseDao.findByPage(page, pageSize);
    }

    public List<Purchase> findByDateRange(String start, String end) {
        return purchaseDao.findByDateRange(start, end);
    }

    public List<Purchase> findByProductId(int productId) {
        return purchaseDao.findByProductId(productId);
    }

    public List<Purchase> findBySupplierId(int supplierId) {
        return purchaseDao.findBySupplierId(supplierId);
    }

    public int getTotalCount() { return purchaseDao.getTotalCount(); }
}
