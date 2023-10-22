package com.training.eshop.dao;

import com.training.eshop.model.User;

import java.util.List;

public interface UserDAO {

    void save(User user);

    User getByLogin(String login);

    List<User> getAll();
}
