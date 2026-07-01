package com.supermarket.dao.impl;

import com.supermarket.dao.CategoryDao;
import com.supermarket.entity.Category;
import java.util.List;

public class CategoryDaoFileImpl extends FileDaoBase<Category> implements CategoryDao {

    public CategoryDaoFileImpl() { super("categories"); }

    @Override public boolean add(Category c) {
        List<Category> list = loadAll(); c.setId(generateId(list)); list.add(c); return saveAll(list);
    }
    @Override public boolean update(Category c) {
        List<Category> list = loadAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == c.getId()) { list.set(i, c); return saveAll(list); }
        } return false;
    }
    @Override public boolean delete(int id) {
        List<Category> list = loadAll();
        boolean r = list.removeIf(c -> c.getId() == id); if (r) saveAll(list); return r;
    }
    @Override public Category findById(int id) {
        return loadAll().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }
    @Override public List<Category> findAll() { return loadAll(); }
    @Override public List<Category> findByPage(int page, int pageSize) {
        List<Category> all = loadAll(); int s = (page-1)*pageSize; int e = Math.min(s+pageSize, all.size());
        return s >= all.size() ? List.of() : all.subList(s, e);
    }
    @Override public int getTotalCount() { return loadAll().size(); }
    @Override public Category findByName(String name) {
        return loadAll().stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
    }
}
