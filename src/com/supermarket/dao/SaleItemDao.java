package com.supermarket.dao;

import com.supermarket.entity.SaleItem;
import java.util.List;

public interface SaleItemDao extends BaseDao<SaleItem> {
    List<SaleItem> findBySaleId(int saleId);
}
