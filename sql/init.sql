-- ========================================
-- 超市管理系统 - SQL Server 建表脚本
-- ========================================

USE supermarket;
GO

-- 用户表
IF OBJECT_ID('users', 'U') IS NOT NULL DROP TABLE users;
CREATE TABLE users (
    id          INT IDENTITY(1,1) PRIMARY KEY,
    username    NVARCHAR(50)  NOT NULL UNIQUE,
    password    NVARCHAR(100) NOT NULL,
    role        NVARCHAR(20)  NOT NULL DEFAULT N'员工',
    phone       NVARCHAR(20)  NULL,
    status      INT           NOT NULL DEFAULT 1
);
GO

-- 分类表
IF OBJECT_ID('categories', 'U') IS NOT NULL DROP TABLE categories;
CREATE TABLE categories (
    id   INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) NOT NULL UNIQUE
);
GO

-- 商品表
IF OBJECT_ID('products', 'U') IS NOT NULL DROP TABLE products;
CREATE TABLE products (
    id              INT IDENTITY(1,1) PRIMARY KEY,
    name            NVARCHAR(100) NOT NULL,
    category_id     INT           NULL,
    manufacturer    NVARCHAR(100) NULL,
    production_date NVARCHAR(20)  NULL,
    expiry_date     NVARCHAR(20)  NULL,
    purchase_date   NVARCHAR(20)  NULL,
    cost_price      DECIMAL(10,2) NOT NULL DEFAULT 0,
    selling_price   DECIMAL(10,2) NOT NULL DEFAULT 0,
    stock           INT           NOT NULL DEFAULT 0,
    sales_volume    INT           NOT NULL DEFAULT 0,
    min_stock       INT           NOT NULL DEFAULT 0,
    is_deleted      INT           NOT NULL DEFAULT 0
);
GO

-- 供应商表
IF OBJECT_ID('suppliers', 'U') IS NOT NULL DROP TABLE suppliers;
CREATE TABLE suppliers (
    id      INT IDENTITY(1,1) PRIMARY KEY,
    name    NVARCHAR(100) NOT NULL,
    phone   NVARCHAR(50)  NULL,
    address NVARCHAR(200) NULL
);
GO

-- 供应关系表
IF OBJECT_ID('supplies', 'U') IS NOT NULL DROP TABLE supplies;
CREATE TABLE supplies (
    id          INT IDENTITY(1,1) PRIMARY KEY,
    product_id  INT NOT NULL,
    supplier_id INT NOT NULL
);
GO

-- 销售记录表
IF OBJECT_ID('sales', 'U') IS NOT NULL DROP TABLE sales;
CREATE TABLE sales (
    id            INT IDENTITY(1,1) PRIMARY KEY,
    total_amount  DECIMAL(10,2) NOT NULL DEFAULT 0,
    sale_time     NVARCHAR(30)  NOT NULL,
    operator_id   INT           NOT NULL
);
GO

-- 销售明细表
IF OBJECT_ID('sale_items', 'U') IS NOT NULL DROP TABLE sale_items;
CREATE TABLE sale_items (
    id           INT IDENTITY(1,1) PRIMARY KEY,
    sale_id      INT           NOT NULL,
    product_id   INT           NOT NULL,
    product_name NVARCHAR(100) NULL,
    quantity     INT           NOT NULL DEFAULT 1,
    subtotal     DECIMAL(10,2) NOT NULL DEFAULT 0
);
GO

-- 进货记录表
IF OBJECT_ID('purchases', 'U') IS NOT NULL DROP TABLE purchases;
CREATE TABLE purchases (
    id            INT IDENTITY(1,1) PRIMARY KEY,
    product_id    INT           NOT NULL,
    supplier_id   INT           NULL,
    quantity      INT           NOT NULL DEFAULT 0,
    unit_price    DECIMAL(10,2) NOT NULL DEFAULT 0,
    total_price   DECIMAL(10,2) NOT NULL DEFAULT 0,
    purchase_date NVARCHAR(30)  NOT NULL,
    operator_id   INT           NULL
);
GO

-- 插入默认管理员（密码: admin123 → MD5: 0192023a7bbd73250516f069df18b500）
IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin')
    INSERT INTO users (username, password, role, phone, status)
    VALUES ('admin', '0192023a7bbd73250516f069df18b500', N'管理员', '', 1);
GO

PRINT '✅ 数据库表创建完成';
GO
