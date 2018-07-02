package com.june.dao;

import com.june.vo.User;

import java.util.List;

public interface UserDao {
    User getUserByUserName(String userName);

    List<String> getRolesByUserName(String userName);

    List<String> getPermissionsByUserName(String userName);
}
