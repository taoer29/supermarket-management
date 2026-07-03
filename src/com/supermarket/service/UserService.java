package com.supermarket.service;

import com.supermarket.dao.UserDao;
import com.supermarket.dao.impl.UserDaoFileImpl;
import com.supermarket.dao.impl.sql.UserDaoSqlImpl;
import com.supermarket.entity.User;
import com.supermarket.util.Config;

import java.util.List;

/**
 * 用户业务逻辑层
 */
public class UserService {
    private final UserDao userDao = Config.isSqlMode() ? new UserDaoSqlImpl() : new UserDaoFileImpl();

    public boolean addUser(User user) {
        if (userDao.findByUsername(user.getUsername()) != null) {
            return false; // 用户名已存在
        }
        return userDao.add(user);
    }

    public boolean updateUser(User user) {
        return userDao.update(user);
    }

    public boolean deleteUser(int id) {
        return userDao.delete(id);
    }

    public User login(String username, String password) {
        return userDao.login(username, password);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public List<User> findByPage(int page, int pageSize) {
        return userDao.findByPage(page, pageSize);
    }

    public int getTotalCount() {
        return userDao.getTotalCount();
    }
}
