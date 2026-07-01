package com.supermarket.dao.impl;

import com.supermarket.dao.UserDao;
import com.supermarket.entity.User;
import com.supermarket.util.MD5Util;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户数据文件存储实现
 * 后续接入SQL：新建 UserDaoSqlImpl 实现 UserDao 接口即可
 */
public class UserDaoFileImpl extends FileDaoBase<User> implements UserDao {

    public UserDaoFileImpl() {
        super("users");
    }

    @Override
    public boolean add(User user) {
        List<User> list = loadAll();
        user.setId(generateId(list));
        user.setPassword(MD5Util.encrypt(user.getPassword()));
        list.add(user);
        return saveAll(list);
    }

    @Override
    public boolean update(User user) {
        List<User> list = loadAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == user.getId()) {
                list.set(i, user);
                return saveAll(list);
            }
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        List<User> list = loadAll();
        boolean removed = list.removeIf(u -> u.getId() == id);
        if (removed) saveAll(list);
        return removed;
    }

    @Override
    public User findById(int id) {
        return loadAll().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<User> findAll() {
        return loadAll();
    }

    @Override
    public List<User> findByPage(int page, int pageSize) {
        List<User> all = loadAll();
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, all.size());
        if (start >= all.size()) return List.of();
        return all.subList(start, end);
    }

    @Override
    public int getTotalCount() {
        return loadAll().size();
    }

    @Override
    public User findByUsername(String username) {
        return loadAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst().orElse(null);
    }

    @Override
    public User login(String username, String password) {
        String encrypted = MD5Util.encrypt(password);
        return loadAll().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(encrypted) && u.getStatus() == 1)
                .findFirst().orElse(null);
    }
}
