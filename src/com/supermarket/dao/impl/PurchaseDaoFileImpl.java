package com.supermarket.dao.impl;

import com.supermarket.dao.PurchaseDao;
import com.supermarket.entity.Purchase;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseDaoFileImpl extends FileDaoBase<Purchase> implements PurchaseDao {

    public PurchaseDaoFileImpl() { super("purchases"); }

    @Override public boolean add(Purchase p) {
        List<Purchase> list = loadAll(); p.setId(generateId(list)); list.add(p); return saveAll(list);
    }
    @Override public boolean update(Purchase p) {
        List<Purchase> list = loadAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == p.getId()) { list.set(i, p); return saveAll(list); }
        } return false;
    }
    @Override public boolean delete(int id) {
        List<Purchase> list = loadAll();
        boolean r = list.removeIf(p -> p.getId() == id); if (r) saveAll(list); return r;
    }
    @Override public Purchase findById(int id) {
        return loadAll().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }
    @Override public List<Purchase> findAll() { return loadAll(); }
    @Override public List<Purchase> findByPage(int page, int pageSize) {
        List<Purchase> all = loadAll(); int s = (page-1)*pageSize; int e = Math.min(s+pageSize, all.size());
        return s >= all.size() ? List.of() : all.subList(s, e);
    }
    @Override public int getTotalCount() { return loadAll().size(); }
    @Override public List<Purchase> findByDateRange(String start, String end) {
        return loadAll().stream()
                .filter(p -> p.getPurchaseDate() != null && p.getPurchaseDate().compareTo(start) >= 0 && p.getPurchaseDate().compareTo(end) <= 0)
                .collect(Collectors.toList());
    }
    @Override public List<Purchase> findByProductId(int productId) {
        return loadAll().stream().filter(p -> p.getProductId() == productId).collect(Collectors.toList());
    }
    @Override public List<Purchase> findBySupplierId(int supplierId) {
        return loadAll().stream().filter(p -> p.getSupplierId() == supplierId).collect(Collectors.toList());
    }
}
