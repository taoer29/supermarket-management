package com.supermarket.dao.impl;

import com.supermarket.dao.SaleDao;
import com.supermarket.entity.Sale;
import java.util.List;
import java.util.stream.Collectors;

public class SaleDaoFileImpl extends FileDaoBase<Sale> implements SaleDao {

    public SaleDaoFileImpl() { super("sales"); }

    @Override public boolean add(Sale s) {
        List<Sale> list = loadAll(); s.setId(generateId(list)); list.add(s); return saveAll(list);
    }
    @Override public boolean update(Sale s) {
        List<Sale> list = loadAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == s.getId()) { list.set(i, s); return saveAll(list); }
        } return false;
    }
    @Override public boolean delete(int id) {
        List<Sale> list = loadAll();
        boolean r = list.removeIf(s -> s.getId() == id); if (r) saveAll(list); return r;
    }
    @Override public Sale findById(int id) {
        return loadAll().stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }
    @Override public List<Sale> findAll() { return loadAll(); }
    @Override public List<Sale> findByPage(int page, int pageSize) {
        List<Sale> all = loadAll(); int s = (page-1)*pageSize; int e = Math.min(s+pageSize, all.size());
        return s >= all.size() ? List.of() : all.subList(s, e);
    }
    @Override public int getTotalCount() { return loadAll().size(); }
    @Override public List<Sale> findByDateRange(String start, String end) {
        return loadAll().stream()
                .filter(s -> s.getSaleTime() != null && s.getSaleTime().compareTo(start) >= 0 && s.getSaleTime().compareTo(end) <= 0)
                .collect(Collectors.toList());
    }
    @Override public List<Sale> findByOperatorId(int operatorId) {
        return loadAll().stream().filter(s -> s.getOperatorId() == operatorId).collect(Collectors.toList());
    }
}
