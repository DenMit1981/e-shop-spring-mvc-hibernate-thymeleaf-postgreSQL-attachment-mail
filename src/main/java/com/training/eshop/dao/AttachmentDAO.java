package com.training.eshop.dao;

import com.training.eshop.model.Attachment;

import java.util.List;

public interface AttachmentDAO {

    void save(Attachment attachment);

    Attachment getByAttachmentIdAndOrderId(Long attachmentId, Long orderId);

    List<Attachment> getAllByOrderId(Long orderId);

    void deleteByAttachmentNameAndOrderId(String attachmentName, Long orderId);
}
