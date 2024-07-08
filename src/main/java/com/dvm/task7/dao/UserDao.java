package com.dvm.task7.dao;

import com.dvm.task7.model.User;

import java.util.List;

public interface UserDao {
    public String insertUser(User user);
    public Boolean updateUser(User user, int id);
    public Boolean deleteUserById(int id);
    public User findUserById(int id);
    public User findUserByName(String name);
    public List<User> findUserByAddress(String address);
    public List<User> sortUsersByName();
    public String validateUser(User user);
//    public Boolean generateRandomUsers(int n);
    public Boolean updateMoney(int id, long money);
}
