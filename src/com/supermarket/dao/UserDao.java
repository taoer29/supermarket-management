package com.supermarket.dao;

import com.supermarket.entity.User;

/**
 * 用户数据访问接口
 */
public interface UserDao extends BaseDao<User> {
    User findByUsername(String username);
    User login(String username, String password);
}
