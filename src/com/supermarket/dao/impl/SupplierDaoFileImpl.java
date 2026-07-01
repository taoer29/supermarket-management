package com.supermarket.dao.impl;

import com.supermarket.dao.SupplierDao;
import com.supermarket.entity.Supplier;
import java.util.List;

public class SupplierDaoFileImpl extends FileDaoBase<Supplier> implements SupplierDao {

    public SupplierDaoFileImpl() { super("suppliers"); }

    @Override public boolean add(Supplier s) {
        List<Supplier> list = loadAll(); s.setId(generateId(list)); list.add(s); return saveAll(list);
    }
    @Override public boolean update(Supplier s) {
        List<Supplier> list = loadAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == s.getId()) { list.set(i, s); return saveAll(list); }
        } return false;
    }
    @Override public boolean delete(int id) {
        List<Supplier> list = loadAll();
        boolean r = list.removeIf(s -> s.getId() == id); if (r) saveAll(list); return r;
    }
    @Override public Supplier findById(int id) {
        return loadAll().stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }
    @Override public List<Supplier> findAll() { return loadAll(); }
    @Override public List<Supplier> findByPage(int page, int pageSize) {
        List<Supplier> all = loadAll(); int s = (page-1)*pageSize; int e = Math.min(s+pageSize, all.size());
        return s >= all.size() ? List.of() : all.subList(s, e);
    }
    @Override public int getTotalCount() { return loadAll().size(); }
    @Override public Supplier findByName(String name) {
        return loadAll().stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
    }
}
