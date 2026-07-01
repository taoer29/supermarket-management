package com.supermarket.dao;

import com.supermarket.entity.Product;
import java.util.List;

/**
 * 商品数据访问接口
 */
public interface ProductDao extends BaseDao<Product> {
    List<Product> searchByName(String keyword);
    List<Product> findByCategoryId(int categoryId);
    List<Product> findLowStock();          // 库存预警列表
    List<Product> findDeleted();            // 已删除商品列表
    boolean restore(int id);                // 恢复已删除商品
    boolean updateStock(int id, int quantity);
}
