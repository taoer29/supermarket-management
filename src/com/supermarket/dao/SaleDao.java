package com.supermarket.dao;

import com.supermarket.entity.Sale;
import java.util.List;

/**
 * 销售记录数据访问接口
 */
public interface SaleDao extends BaseDao<Sale> {
    List<Sale> findByDateRange(String start, String end);
    List<Sale> findByOperatorId(int operatorId);
}
