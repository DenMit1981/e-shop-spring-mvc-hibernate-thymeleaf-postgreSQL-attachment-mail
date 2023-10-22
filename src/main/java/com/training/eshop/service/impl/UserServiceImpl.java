package com.training.eshop.service.impl;

import com.training.eshop.model.User;
import com.training.eshop.service.UserService;
import com.training.eshop.converter.UserConverter;
import com.training.eshop.dao.UserDAO;
import com.training.eshop.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class.getName());

    private static final String FIELD_IS_EMPTY = "Fields shouldn't be empty";
    private static final String INVALID_LOGIN_OR_PASSWORD = "Login or password shouldn't be less than 4 symbols";
    private static final String INVALID_NUMBER_OF_SYMBOLS_FOR_EMAIL = "Email shouldn't be less than 6 symbols";
    private static final String INVALID_EMAIL = "Email should contain symbol @";
    private static final String USER_IS_PRESENT = "User with login {} or email {} is already present";


    private final UserDAO userDAO;
    private final UserConverter userConverter;

    @Override
    @Transactional
    public void save(UserDto userDto) {
        User user = userConverter.fromUserDto(userDto);

        userDAO.save(user);

        LOGGER.info("New user : {}", user);
    }

    @Override
    @Transactional
    public User getByLogin(String login) {
        return userDAO.getByLogin(login);
    }

    @Override
    @Transactional
    public boolean isInvalidUser(UserDto userDto) {
        return userDto.getLogin().length() < 4 || userDto.getPassword().length() < 4 ||
                userDto.getEmail().length() < 6 || !userDto.getEmail().contains("@") ||
                isUserPresent(userDto);
    }

    @Override
    @Transactional
    public String invalidUser(UserDto userDto) {
        String login = userDto.getLogin();
        String password = userDto.getPassword();
        String email = userDto.getEmail();

        if (login.isEmpty() || password.isEmpty() || email.isEmpty()) {
            LOGGER.error(FIELD_IS_EMPTY);

            return FIELD_IS_EMPTY;
        }

        if (isUserPresent(userDto)) {
            LOGGER.error(USER_IS_PRESENT, login, email);

            return String.format(USER_IS_PRESENT.replace("{}", "%s"), login, email);
        }

        if (email.length() < 6) {
            LOGGER.error(INVALID_NUMBER_OF_SYMBOLS_FOR_EMAIL);

            return INVALID_NUMBER_OF_SYMBOLS_FOR_EMAIL;
        }

        if (!email.contains("@")) {
            LOGGER.error(INVALID_EMAIL);

            return INVALID_EMAIL;
        }

        LOGGER.error(INVALID_LOGIN_OR_PASSWORD);

        return INVALID_LOGIN_OR_PASSWORD;
    }

    @Transactional
    public boolean isUserPresent(UserDto userDto) {
        String login = userDto.getLogin();
        String email = userDto.getEmail();

        return userDAO.getAll().stream().anyMatch(user -> login.equals(user.getLogin())
                || email.equals(user.getEmail()));
    }
}
