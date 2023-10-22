package com.training.eshop.dao.impl;

import com.training.eshop.dao.AttachmentDAO;
import com.training.eshop.model.Attachment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@AllArgsConstructor
public class AttachmentDAOImpl implements AttachmentDAO {

    private static final String ORDER_ID = "orderId";
    private static final String ATTACHMENT_ID = "attachmentId";
    private static final String ATTACHMENT_NAME = "attachmentName";
    private static final String QUERY_SELECT_FROM_ATTACHMENT_BY_ORDER_ID = "from Attachment a where a.order.id =:orderId";
    private static final String QUERY_SELECT_FROM_ATTACHMENT_BY_ORDER_ID_AND_ATTACHMENT_ID = "from Attachment a where a.order.id =:orderId and a.id =:attachmentId";
    private static final String QUERY_DELETE_FROM_ATTACHMENT_BY_ORDER_ID_AND_ATTACHMENT_NAME = "delete from Attachment a where a.order.id =:orderId and a.name =:attachmentName";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void save(Attachment attachment) {
        entityManager.persist(attachment);
    }

    @Override
    public Attachment getByAttachmentIdAndOrderId(Long attachmentId, Long orderId) {
        return (Attachment) entityManager.createQuery(QUERY_SELECT_FROM_ATTACHMENT_BY_ORDER_ID_AND_ATTACHMENT_ID)
                .setParameter(ATTACHMENT_ID, attachmentId)
                .setParameter(ORDER_ID, orderId)
                .getSingleResult();
    }

    @Override
    public List<Attachment> getAllByOrderId(Long orderId) {
        return entityManager.createQuery(QUERY_SELECT_FROM_ATTACHMENT_BY_ORDER_ID)
                .setParameter(ORDER_ID, orderId)
                .getResultList();
    }

    @Override
    public void deleteByAttachmentNameAndOrderId(String attachmentName, Long orderId) {
        entityManager.createQuery(QUERY_DELETE_FROM_ATTACHMENT_BY_ORDER_ID_AND_ATTACHMENT_NAME)
                .setParameter(ATTACHMENT_NAME, attachmentName)
                .setParameter(ORDER_ID, orderId)
                .executeUpdate();
    }
}
