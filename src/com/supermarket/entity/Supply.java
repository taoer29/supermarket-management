package com.supermarket.entity;

import java.io.Serializable;

/**
 * 供应关系实体类（商品与供应商多对多关系）
 */
public class Supply implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int productId;
    private int supplierId;

    public Supply() {}

    public Supply(int productId, int supplierId) {
        this.productId = productId;
        this.supplierId = supplierId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
}
