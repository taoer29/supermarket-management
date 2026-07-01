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
src/com/supermarket/
├── App.java              # 启动入口
├── entity/               # 实体类
├── dao/                  # 数据访问接口 + 文件实现
├── service/              # 业务逻辑层
├── ui/                   # Swing界面
└── util/                 # 工具类
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
