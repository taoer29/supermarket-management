package com.supermarket.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 进货记录实体类
 */
public class Purchase implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int productId;
    private int supplierId;
    private int quantity;            // 进货数量
    private BigDecimal unitPrice;    // 单价
    private BigDecimal totalPrice;   // 总金额
    private String purchaseDate;     // 进货日期
    private int operatorId;          // 操作员ID

    public Purchase() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }

    public int getOperatorId() { return operatorId; }
    public void setOperatorId(int operatorId) { this.operatorId = operatorId; }

    @Override
    public String toString() {
        return "Purchase{id=" + id + ", productId=" + productId + ", quantity=" + quantity + "}";
    }
}
