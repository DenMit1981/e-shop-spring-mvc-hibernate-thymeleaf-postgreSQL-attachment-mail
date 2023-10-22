package com.training.eshop.service;

import com.training.eshop.dto.UserDto;
import com.training.eshop.model.User;

public interface UserService {

    void save(UserDto userDto);

    User getByLogin(String login);

    boolean isInvalidUser(UserDto userDto);

    String invalidUser(UserDto userDto);
}
