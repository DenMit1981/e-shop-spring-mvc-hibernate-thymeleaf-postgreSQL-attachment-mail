package com.training.eshop.service;

import com.training.eshop.dto.AttachmentDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public interface AttachmentService {

    List<AttachmentDto> getAllByOrderId(Long orderId);

    AttachmentDto getById(Long attachmentId, Long orderId);

    String showChosenFiles(List<AttachmentDto> chosenFiles, Long orderId);

    AttachmentDto getChosenAttachment(MultipartFile file, HttpSession session, Long orderId) throws IOException;

    void getEventsWithAttachment(AttachmentDto attachmentDto, String command, HttpSession session, Long orderId);
}
