package com.supermarket.dao;

import java.util.List;

/**
 * 通用数据访问接口
 * 后续接入SQL数据库时，新建SQL实现类实现此接口即可
 */
public interface BaseDao<T> {
    boolean add(T t);
    boolean update(T t);
    boolean delete(int id);
    T findById(int id);
    List<T> findAll();
    List<T> findByPage(int page, int pageSize);
    int getTotalCount();
}
