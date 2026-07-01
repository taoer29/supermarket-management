package com.supermarket.dao;

import com.supermarket.entity.Supply;
import java.util.List;

/**
 * 供应关系数据访问接口
 */
public interface SupplyDao extends BaseDao<Supply> {
    List<Supply> findByProductId(int productId);
    List<Supply> findBySupplierId(int supplierId);
    boolean deleteByProductId(int productId);
}
