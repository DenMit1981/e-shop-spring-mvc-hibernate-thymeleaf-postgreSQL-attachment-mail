package com.training.eshop.dao.impl;

import com.training.eshop.dao.OrderDAO;
import com.training.eshop.model.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@AllArgsConstructor
public class OrderDAOImpl implements OrderDAO {

    private static final String QUERY_SELECT_FROM_ORDER = "from Order";
    private static final String QUERY_SELECT_FROM_ORDER_BY_ID = "from Order o where o.id = :id";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void save(Order order) {
        entityManager.persist(order);
    }

    @Override
    public Order getById(Long id) {
        return (Order) entityManager.createQuery(QUERY_SELECT_FROM_ORDER_BY_ID)
                .setParameter("id", id).getSingleResult();
    }

    @Override
    public List<Order> getAll() {
        return entityManager.createQuery(QUERY_SELECT_FROM_ORDER).getResultList();
    }
}
