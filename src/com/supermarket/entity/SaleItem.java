package com.supermarket.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 销售明细实体类
 */
public class SaleItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int saleId;
    private int productId;
    private String productName;  // 冗余字段，方便显示
    private int quantity;        // 数量
    private BigDecimal subtotal; // 小计

    public SaleItem() {}

    public SaleItem(int productId, String productName, int quantity, BigDecimal price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
