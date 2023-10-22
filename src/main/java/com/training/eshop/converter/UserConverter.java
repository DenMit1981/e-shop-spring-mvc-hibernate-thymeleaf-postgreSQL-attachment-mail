package com.training.eshop.converter;

import com.training.eshop.model.User;
import com.training.eshop.dto.UserDto;

public interface UserConverter {

    User fromUserDto(UserDto userDto);
}
