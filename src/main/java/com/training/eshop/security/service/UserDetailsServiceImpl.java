package com.training.eshop.security.service;

import com.training.eshop.model.User;
import com.training.eshop.dao.UserDAO;
import com.training.eshop.security.model.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOGGER = LogManager.getLogger(UserDetailsServiceImpl.class.getName());

    private final UserDAO userDAO;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) {
        User user = userDAO.getByLogin(login);

        LOGGER.info("User : {}", user);

        if (user == null) {
            throw new UsernameNotFoundException(login);
        }

        return new CustomUserDetails(user);
    }
}

