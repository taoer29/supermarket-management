package com.supermarket.entity;

import java.io.Serializable;

/**
 * 供应商实体类
 */
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;     // 供应商名称
    private String phone;    // 联系电话
    private String address;  // 地址

    public Supplier() {}

    public Supplier(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "Supplier{id=" + id + ", name='" + name + "'}";
    }
}
