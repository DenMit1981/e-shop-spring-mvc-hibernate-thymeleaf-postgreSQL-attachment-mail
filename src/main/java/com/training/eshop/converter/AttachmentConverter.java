package com.training.eshop.converter;

import com.training.eshop.dto.AttachmentDto;
import com.training.eshop.model.Attachment;

public interface AttachmentConverter {

    AttachmentDto convertToAttachmentDto(Attachment attachment);

    Attachment fromAttachmentDto(AttachmentDto attachmentDto, Long orderId);
}
