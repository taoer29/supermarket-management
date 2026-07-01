package com.supermarket.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品实体类
 */
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;             // 商品名称
    private int categoryId;          // 分类ID
    private String manufacturer;     // 生产厂家
    private String productionDate;   // 生产日期
    private String expiryDate;       // 过期日期
    private String purchaseDate;     // 入库日期
    private BigDecimal costPrice;    // 进价
    private BigDecimal sellingPrice; // 售价
    private int stock;               // 库存数量
    private int salesVolume;         // 销量
    private int minStock;            // 最低库存预警
    private int isDeleted;           // 0-正常 1-删除

    public Product() {}

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getProductionDate() { return productionDate; }
    public void setProductionDate(String productionDate) { this.productionDate = productionDate; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }

    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getSalesVolume() { return salesVolume; }
    public void setSalesVolume(int salesVolume) { this.salesVolume = salesVolume; }

    public int getMinStock() { return minStock; }
    public void setMinStock(int minStock) { this.minStock = minStock; }

    public int getIsDeleted() { return isDeleted; }
    public void setIsDeleted(int isDeleted) { this.isDeleted = isDeleted; }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', stock=" + stock + "}";
    }
}
