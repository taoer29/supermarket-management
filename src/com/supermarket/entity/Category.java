package com.supermarket.entity;

import java.io.Serializable;

/**
 * 商品分类实体类
 */
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name; // 分类名称

    public Category() {}

    public Category(String name) { this.name = name; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Category{id=" + id + ", name='" + name + "'}";
    }
}
