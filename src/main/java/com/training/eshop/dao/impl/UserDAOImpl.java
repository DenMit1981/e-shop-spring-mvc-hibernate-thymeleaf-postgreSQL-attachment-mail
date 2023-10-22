package com.training.eshop.dao.impl;

import com.training.eshop.dao.UserDAO;
import com.training.eshop.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@AllArgsConstructor
public class UserDAOImpl implements UserDAO {

    private static final String QUERY_SELECT_FROM_USER = "from User";
    private static final String QUERY_SELECT_FROM_USER_BY_LOGIN = "from User u where u.login = :login";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public User getByLogin(String login) {
        return (User) entityManager.createQuery(QUERY_SELECT_FROM_USER_BY_LOGIN)
                .setParameter("login", login).getSingleResult();
    }

    @Override
    public List<User> getAll() {
        return entityManager.createQuery(QUERY_SELECT_FROM_USER).getResultList();
    }
}
