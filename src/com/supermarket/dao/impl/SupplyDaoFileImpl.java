package com.supermarket.dao.impl;

import com.supermarket.dao.SupplyDao;
import com.supermarket.entity.Supply;
import java.util.List;
import java.util.stream.Collectors;

public class SupplyDaoFileImpl extends FileDaoBase<Supply> implements SupplyDao {

    public SupplyDaoFileImpl() { super("supplies"); }

    @Override public boolean add(Supply s) {
        List<Supply> list = loadAll(); s.setId(generateId(list)); list.add(s); return saveAll(list);
    }
    @Override public boolean update(Supply s) {
        List<Supply> list = loadAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == s.getId()) { list.set(i, s); return saveAll(list); }
        } return false;
    }
    @Override public boolean delete(int id) {
        List<Supply> list = loadAll();
        boolean r = list.removeIf(s -> s.getId() == id); if (r) saveAll(list); return r;
    }
    @Override public boolean deleteByProductId(int productId) {
        List<Supply> list = loadAll();
        boolean r = list.removeIf(s -> s.getProductId() == productId); if (r) saveAll(list); return r;
    }
    @Override public Supply findById(int id) {
        return loadAll().stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }
    @Override public List<Supply> findAll() { return loadAll(); }
    @Override public List<Supply> findByPage(int page, int pageSize) {
        List<Supply> all = loadAll(); int s = (page-1)*pageSize; int e = Math.min(s+pageSize, all.size());
        return s >= all.size() ? List.of() : all.subList(s, e);
    }
    @Override public int getTotalCount() { return loadAll().size(); }
    @Override public List<Supply> findByProductId(int productId) {
        return loadAll().stream().filter(s -> s.getProductId() == productId).collect(Collectors.toList());
    }
    @Override public List<Supply> findBySupplierId(int supplierId) {
        return loadAll().stream().filter(s -> s.getSupplierId() == supplierId).collect(Collectors.toList());
    }
}
