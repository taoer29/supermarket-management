package com.supermarket.dao.impl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件存储基类——使用Java序列化读写.dat文件
 * 后续接入SQL：新建 dao/impl/sql/ 包，实现相同接口即可，
 * 然后修改 Service 里的 new XxxDaoFileImpl() 为 new XxxDaoSqlImpl()
 */
public abstract class FileDaoBase<T> {

    protected final String filePath;

    public FileDaoBase(String fileName) {
        // 数据文件存放在项目根目录的 data/ 文件夹下
        this.filePath = "data" + File.separator + fileName + ".dat";
    }

    @SuppressWarnings("unchecked")
    protected List<T> loadAll() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    protected boolean saveAll(List<T> list) {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(list);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected int generateId(List<T> list) {
        return list.size() + 1; // 简化版ID生成
    }
}
