package com.training.eshop.converter.impl;

import com.training.eshop.converter.UserConverter;
import com.training.eshop.model.User;
import com.training.eshop.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserConverterImpl implements UserConverter {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User fromUserDto(UserDto userDto) {
        User user = new User();

        user.setLogin(userDto.getLogin());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());

        return user;
    }
}
