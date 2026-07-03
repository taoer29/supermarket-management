package com.supermarket.util;

/**
 * 系统配置——切换数据存储模式
 *
 * 当前支持：
 * - FILE：文件序列化存储（默认，无需数据库）
 * - SQL：SQL Server 数据库存储（需安装SQL Server并运行sql/init.sql）
 *
 * 切换方式：修改 DATA_MODE 的值即可
 */
public class Config {

    /** 数据存储模式 */
    public static final DataMode DATA_MODE = DataMode.SQL;

    public enum DataMode {
        FILE,   // 文件序列化（.dat）
        SQL     // SQL Server 数据库
    }

    /**
     * 判断当前是否为SQL模式
     */
    public static boolean isSqlMode() {
        return DATA_MODE == DataMode.SQL;
    }
}
