package com.supermarket.util;

import com.supermarket.dao.impl.*;
import com.supermarket.dao.impl.sql.*;
import com.supermarket.entity.*;
import com.supermarket.util.MD5Util;

import java.util.List;

/**
 * 数据迁移工具——将文件存储（.dat）数据迁移到 SQL Server
 *
 * 使用方式：切换 Config.DATA_MODE = SQL 后，运行 main 方法即可
 */
public class DataMigration {

    public static void main(String[] args) {
        System.out.println("===== 开始数据迁移：文件 → SQL Server =====");

        // 强制使用文件模式读取
        if (Config.isSqlMode()) {
            System.out.println("⚠ 请先将 Config.DATA_MODE 改为 FILE，运行迁移，再改回 SQL");
            return;
        }

        // 目标：SQL 写入器
        var userSql = new UserDaoSqlImpl();
        var categorySql = new CategoryDaoSqlImpl();
        var productSql = new ProductDaoSqlImpl();
        var supplierSql = new SupplierDaoSqlImpl();
        var supplySql = new SupplyDaoSqlImpl();
        var saleSql = new SaleDaoSqlImpl();
        var saleItemSql = new SaleItemDaoSqlImpl();
        var purchaseSql = new PurchaseDaoSqlImpl();

        // 源：文件读取器
        var userFile = new UserDaoFileImpl();
        var categoryFile = new CategoryDaoFileImpl();
        var productFile = new ProductDaoFileImpl();
        var supplierFile = new SupplierDaoFileImpl();
        var supplyFile = new SupplyDaoFileImpl();
        var saleFile = new SaleDaoFileImpl();
        var saleItemFile = new SaleItemDaoFileImpl();
        var purchaseFile = new PurchaseDaoFileImpl();

        // 1. 迁移分类
        migrate("分类", categoryFile.findAll(), c -> {
            var sql = new Category();
            sql.setName(c.getName());
            categorySql.add(sql);
        });

        // 2. 迁移用户
        migrate("用户", userFile.findAll(), u -> {
            var sql = new User(u.getUsername(), u.getPassword(), u.getRole());
            sql.setPassword(u.getPassword()); // 已经是MD5
            sql.setPhone(u.getPhone());
            sql.setStatus(u.getStatus());
            userSql.add(sql);
        });

        // 3. 迁移商品
        migrate("商品", productFile.findAll(), p -> {
            var sql = new Product();
            copyProduct(p, sql);
            productSql.add(sql);
        });

        // 4. 迁移供应商
        migrate("供应商", supplierFile.findAll(), s -> {
            var sql = new Supplier(s.getName(), s.getPhone(), s.getAddress());
            supplierSql.add(sql);
        });

        // 5. 迁移供应关系
        migrate("供应关系", supplyFile.findAll(), s -> {
            var sql = new Supply(s.getProductId(), s.getSupplierId());
            supplySql.add(sql);
        });

        // 6. 迁移销售记录
        migrate("销售记录", saleFile.findAll(), s -> {
            var sql = new Sale();
            sql.setTotalAmount(s.getTotalAmount());
            sql.setSaleTime(s.getSaleTime());
            sql.setOperatorId(s.getOperatorId());
            // 保持原始ID，这样明细能关联上
            saleSql.add(sql);
        });

        // 7. 迁移销售明细
        migrate("销售明细", saleItemFile.findAll(), item -> {
            var sql = new SaleItem();
            sql.setSaleId(item.getSaleId());
            sql.setProductId(item.getProductId());
            sql.setProductName(item.getProductName());
            sql.setQuantity(item.getQuantity());
            sql.setSubtotal(item.getSubtotal());
            saleItemSql.add(sql);
        });

        // 8. 迁移进货记录
        migrate("进货记录", purchaseFile.findAll(), p -> {
            var sql = new Purchase();
            sql.setProductId(p.getProductId());
            sql.setSupplierId(p.getSupplierId());
            sql.setQuantity(p.getQuantity());
            sql.setUnitPrice(p.getUnitPrice());
            sql.setTotalPrice(p.getTotalPrice());
            sql.setPurchaseDate(p.getPurchaseDate());
            sql.setOperatorId(p.getOperatorId());
            purchaseSql.add(sql);
        });

        System.out.println("\n===== ✅ 数据迁移完成 =====");
        System.out.println("请将 Config.DATA_MODE 改为 SQL，然后重新启动系统");
    }

    @FunctionalInterface
    interface MigrateAction<T> {
        void migrate(T item);
    }

    private static <T> void migrate(String name, List<T> list, MigrateAction<T> action) {
        int count = 0;
        for (T item : list) {
            try {
                action.migrate(item);
                count++;
            } catch (Exception e) {
                System.out.println("  ⚠ 迁移失败：" + name + " #" + count + " - " + e.getMessage());
            }
        }
        System.out.println("  ✅ " + name + "：已迁移 " + count + " / " + list.size() + " 条");
    }

    private static void copyProduct(Product src, Product dst) {
        dst.setName(src.getName());
        dst.setCategoryId(src.getCategoryId());
        dst.setManufacturer(src.getManufacturer());
        dst.setProductionDate(src.getProductionDate());
        dst.setExpiryDate(src.getExpiryDate());
        dst.setPurchaseDate(src.getPurchaseDate());
        dst.setCostPrice(src.getCostPrice());
        dst.setSellingPrice(src.getSellingPrice());
        dst.setStock(src.getStock());
        dst.setSalesVolume(src.getSalesVolume());
        dst.setMinStock(src.getMinStock());
        dst.setIsDeleted(src.getIsDeleted());
    }
}
