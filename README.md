# 超市管理系统

Java + Swing 课程设计实训项目

## 项目简介

基于 Java 语言开发的超市管理系统，使用 Swing 构建图形用户界面，采用 MVC 分层架构。数据存储支持文件 I/O（序列化）和 MySQL 数据库两种模式，可灵活切换。

## 技术栈

- **语言：** Java (JDK 17+)
- **GUI框架：** Swing (JFrame/JPanel/JTable)
- **数据存储：** 文件序列化 (.dat) / MySQL 8.0 + JDBC
- **IDE：** IntelliJ IDEA
- **版本控制：** Git + GitHub

## 功能模块

| 模块 | 功能 |
|------|------|
| 登录模块 | 用户登录验证、MD5加密、安全锁定 |
| 用户管理 | 用户CRUD、分页查询、角色管理 |
| 商品管理 | 商品CRUD、分类管理、库存预警、分页搜索 |
| 供应商管理 | 供应商CRUD、供应信息查询 |
| 进货管理 | 进货录入、自动关联供应商、进货记录查询 |
| 收银台 | 购物车、结算扣库存、销售记录 |
| 报表统计 | 商品库存HTML报表、销售记录HTML报表 |
| 数据维护 | 数据备份、逻辑删除与恢复 |

## 项目结构

```
com.supermarket/
│
├── App.java                          # 启动入口
│
├── entity/                           # 实体层（POJO）
│   ├── User.java                     # 用户（用户名、密码MD5、角色、手机号、状态）
│   ├── Product.java                  # 商品（名称、分类、进/售价、库存、预警值）
│   ├── Category.java                 # 分类（名称）
│   ├── Supplier.java                 # 供应商（名称、电话、地址）
│   ├── Supply.java                   # 供应关系（商品ID → 供应商ID）
│   ├── Sale.java                     # 销售记录（总金额、时间、操作员 + 明细列表）
│   ├── SaleItem.java                 # 销售明细（商品ID、数量、小计）
│   └── Purchase.java                 # 进货记录（商品、供应商、数量、单价、总金额）
│
├── dao/                              # 数据访问层（接口）
│   ├── BaseDao.java                  # ★ 通用CRUD接口（add/update/delete/findById/findAll/findByPage）
│   ├── UserDao.java                  # 用户 + findByUsername/login
│   ├── ProductDao.java               # 商品 + searchByName/findLowStock/restore/updateStock
│   ├── CategoryDao.java              # 分类 + findByName
│   ├── SupplierDao.java              # 供应商 + findByName
│   ├── SupplyDao.java                # 供应关系 + findByProductId/findBySupplierId
│   ├── SaleDao.java                  # 销售记录 + findByDateRange/findByOperatorId
│   ├── SaleItemDao.java              # 销售明细 + findBySaleId
│   ├── PurchaseDao.java              # 进货记录 + findByDateRange/findByProductId/findBySupplierId
│   │
│   └── impl/                         # ★ 当前实现：文件I/O（序列化.dat）
│       ├── FileDaoBase.java          # 文件读写基类（loadAll/saveAll）
│       ├── UserDaoFileImpl.java
│       ├── ProductDaoFileImpl.java
│       ├── CategoryDaoFileImpl.java
│       ├── SupplierDaoFileImpl.java
│       ├── SupplyDaoFileImpl.java
│       ├── SaleDaoFileImpl.java
│       ├── SaleItemDaoFileImpl.java
│       └── PurchaseDaoFileImpl.java
│       （接入MySQL时：新建 impl/sql/ 包实现同组接口即可）
│
├── service/                          # 业务逻辑层
│   ├── UserService.java              # 用户业务（注册校验、登录）
│   ├── ProductService.java           # 商品业务
│   ├── SupplierService.java          # 供应商业务
│   ├── SaleService.java              # ★ 核心：结算流程（扣库存→生成记录→更新销量）
│   └── FileService.java              # 文件服务（HTML报表导出）
│
├── ui/                               # 视图层（Swing）
│   ├── LoginFrame.java               # 登录窗口（3次错误锁定）
│   ├── MainFrame.java                # 主窗口（JTabbedPane导航）
│   ├── ProductPanel.java             # 商品管理面板（CRUD + 搜索 + 分页 + 预警）
│   ├── UserPanel.java                # 用户管理面板
│   ├── SupplierPanel.java            # 供应商管理面板
│   ├── SalePanel.java                # 收银台面板（购物车 + 结算）
│   └── ReportPanel.java              # 报表面板（HTML导出）
│
└── util/                             # 工具类
    ├── MD5Util.java                  # MD5密码加密
    ├── FileUtil.java                 # HTML报表生成 + 文件写入
    └── DBUtil.java                   # ★ MySQL连接预留（字段已配置，取消注释即用）
```

## 快速开始

1. 用 IntelliJ IDEA 打开项目
2. 运行 `com.supermarket.App.java`
3. 默认管理员账号：`admin` / `admin123`

## 切换至 MySQL

1. 安装 MySQL 8.0，执行 `docs/schema.sql` 创建数据库表
2. 在 `dao/impl/sql/` 下实现 SQL 版本的 DAO 类
3. 修改 `service/` 中对应 Service 的 DAO 引用
4. 配置 `util/DBUtil.java` 中的数据库连接信息

## 团队分工

| 角色 | 职责 |
|------|------|
| 组长/项目经理 | 架构设计、代码规范、模块集成、文档 |
| 成员A | 登录模块 + 用户管理 |
| 成员B | 商品管理（CRUD + 分类 + 库存预警） |
| 成员C | 供应商管理 + 进货管理 |
| 成员D | 收银台 + 报表 + 文件I/O |
