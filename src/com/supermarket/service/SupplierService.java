package com.supermarket.service;

import com.supermarket.dao.SupplierDao;
import com.supermarket.dao.impl.SupplierDaoFileImpl;
import com.supermarket.dao.impl.sql.SupplierDaoSqlImpl;
import com.supermarket.entity.Supplier;
import com.supermarket.util.Config;

import java.util.List;

/**
 * 供应商业务逻辑层
 */
public class SupplierService {
    private final SupplierDao supplierDao = Config.isSqlMode() ? new SupplierDaoSqlImpl() : new SupplierDaoFileImpl();

    public boolean addSupplier(Supplier s) { return supplierDao.add(s); }
    public boolean updateSupplier(Supplier s) { return supplierDao.update(s); }
    public boolean deleteSupplier(int id) { return supplierDao.delete(id); }
    public Supplier findById(int id) { return supplierDao.findById(id); }
    public Supplier findByName(String name) { return supplierDao.findByName(name); }
    public List<Supplier> findAll() { return supplierDao.findAll(); }
    public List<Supplier> findByPage(int page, int pageSize) { return supplierDao.findByPage(page, pageSize); }
    public int getTotalCount() { return supplierDao.getTotalCount(); }
}
