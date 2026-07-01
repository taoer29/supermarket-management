package com.supermarket.dao;

import com.supermarket.entity.Purchase;
import java.util.List;

/**
 * 进货记录数据访问接口
 */
public interface PurchaseDao extends BaseDao<Purchase> {
    List<Purchase> findByDateRange(String start, String end);
    List<Purchase> findByProductId(int productId);
    List<Purchase> findBySupplierId(int supplierId);
}
