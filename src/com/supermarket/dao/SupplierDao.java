package com.supermarket.dao;

import com.supermarket.entity.Supplier;

/**
 * 供应商数据访问接口
 */
public interface SupplierDao extends BaseDao<Supplier> {
    Supplier findByName(String name);
}
