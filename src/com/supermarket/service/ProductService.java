package com.supermarket.service;

import com.supermarket.dao.ProductDao;
import com.supermarket.dao.impl.ProductDaoFileImpl;
import com.supermarket.entity.Product;
import java.util.List;

/**
 * 商品业务逻辑层
 */
public class ProductService {
    private final ProductDao productDao = new ProductDaoFileImpl();

    public boolean addProduct(Product product) {
        return productDao.add(product);
    }

    public boolean updateProduct(Product product) {
        return productDao.update(product);
    }

    public boolean deleteProduct(int id) {
        return productDao.delete(id);
    }

    public boolean restoreProduct(int id) {
        return productDao.restore(id);
    }

    public Product findById(int id) {
        return productDao.findById(id);
    }

    public List<Product> searchByName(String keyword) {
        return productDao.searchByName(keyword);
    }

    public List<Product> findByCategoryId(int categoryId) {
        return productDao.findByCategoryId(categoryId);
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public List<Product> findLowStock() {
        return productDao.findLowStock();
    }

    public List<Product> findDeleted() {
        return productDao.findDeleted();
    }

    public List<Product> findByPage(int page, int pageSize) {
        return productDao.findByPage(page, pageSize);
    }

    public int getTotalCount() {
        return productDao.getTotalCount();
    }

    public boolean updateStock(int id, int quantity) {
        return productDao.updateStock(id, quantity);
    }
}
