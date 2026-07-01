package com.supermarket.dao;

import com.supermarket.entity.Category;

/**
 * 商品分类数据访问接口
 */
public interface CategoryDao extends BaseDao<Category> {
    Category findByName(String name);
}
