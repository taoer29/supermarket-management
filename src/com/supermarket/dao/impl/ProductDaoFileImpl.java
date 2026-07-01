package com.supermarket.dao.impl;

import com.supermarket.dao.ProductDao;
import com.supermarket.entity.Product;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品数据文件存储实现
 */
public class ProductDaoFileImpl extends FileDaoBase<Product> implements ProductDao {

    public ProductDaoFileImpl() {
        super("products");
    }

    @Override
    public boolean add(Product product) {
        List<Product> list = loadAll();
        product.setId(generateId(list));
        list.add(product);
        return saveAll(list);
    }

    @Override
    public boolean update(Product product) {
        List<Product> list = loadAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == product.getId()) {
                list.set(i, product);
                return saveAll(list);
            }
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        // 逻辑删除：is_deleted = 1
        List<Product> list = loadAll();
        for (Product p : list) {
            if (p.getId() == id) {
                p.setIsDeleted(1);
                return saveAll(list);
            }
        }
        return false;
    }

    @Override
    public boolean restore(int id) {
        List<Product> list = loadAll();
        for (Product p : list) {
            if (p.getId() == id) {
                p.setIsDeleted(0);
                return saveAll(list);
            }
        }
        return false;
    }

    @Override
    public Product findById(int id) {
        return loadAll().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Product> findAll() {
        return loadAll().stream().filter(p -> p.getIsDeleted() == 0).collect(Collectors.toList());
    }

    @Override
    public List<Product> findByPage(int page, int pageSize) {
        List<Product> all = findAll();
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, all.size());
        if (start >= all.size()) return List.of();
        return all.subList(start, end);
    }

    @Override
    public int getTotalCount() {
        return findAll().size();
    }

    @Override
    public List<Product> searchByName(String keyword) {
        return findAll().stream()
                .filter(p -> p.getName().contains(keyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCategoryId(int categoryId) {
        return findAll().stream()
                .filter(p -> p.getCategoryId() == categoryId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findLowStock() {
        return findAll().stream()
                .filter(p -> p.getStock() <= p.getMinStock())
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findDeleted() {
        return loadAll().stream().filter(p -> p.getIsDeleted() == 1).collect(Collectors.toList());
    }

    @Override
    public boolean updateStock(int id, int quantity) {
        List<Product> list = loadAll();
        for (Product p : list) {
            if (p.getId() == id) {
                p.setStock(p.getStock() + quantity);
                return saveAll(list);
            }
        }
        return false;
    }
}
