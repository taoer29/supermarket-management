package com.supermarket.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售记录实体类
 */
public class Sale implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private BigDecimal totalAmount;  // 总金额
    private String saleTime;         // 销售时间
    private int operatorId;          // 操作员ID

    private List<SaleItem> items = new ArrayList<>(); // 销售明细

    public Sale() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getSaleTime() { return saleTime; }
    public void setSaleTime(String saleTime) { this.saleTime = saleTime; }

    public int getOperatorId() { return operatorId; }
    public void setOperatorId(int operatorId) { this.operatorId = operatorId; }

    public List<SaleItem> getItems() { return items; }
    public void setItems(List<SaleItem> items) { this.items = items; }

    @Override
    public String toString() {
        return "Sale{id=" + id + ", total=" + totalAmount + ", time='" + saleTime + "'}";
    }
}
