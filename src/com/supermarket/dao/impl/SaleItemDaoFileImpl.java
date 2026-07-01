package com.supermarket.dao.impl;

import com.supermarket.dao.SaleItemDao;
import com.supermarket.entity.SaleItem;
import java.util.List;
import java.util.stream.Collectors;

public class SaleItemDaoFileImpl extends FileDaoBase<SaleItem> implements SaleItemDao {

    public SaleItemDaoFileImpl() { super("sale_items"); }

    @Override public boolean add(SaleItem s) {
        List<SaleItem> list = loadAll(); s.setId(generateId(list)); list.add(s); return saveAll(list);
    }
    @Override public boolean update(SaleItem s) {
        List<SaleItem> list = loadAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == s.getId()) { list.set(i, s); return saveAll(list); }
        } return false;
    }
    @Override public boolean delete(int id) {
        List<SaleItem> list = loadAll();
        boolean r = list.removeIf(s -> s.getId() == id); if (r) saveAll(list); return r;
    }
    @Override public SaleItem findById(int id) {
        return loadAll().stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }
    @Override public List<SaleItem> findAll() { return loadAll(); }
    @Override public List<SaleItem> findByPage(int page, int pageSize) {
        List<SaleItem> all = loadAll(); int s = (page-1)*pageSize; int e = Math.min(s+pageSize, all.size());
        return s >= all.size() ? List.of() : all.subList(s, e);
    }
    @Override public int getTotalCount() { return loadAll().size(); }
    @Override public List<SaleItem> findBySaleId(int saleId) {
        return loadAll().stream().filter(s -> s.getSaleId() == saleId).collect(Collectors.toList());
    }
}
