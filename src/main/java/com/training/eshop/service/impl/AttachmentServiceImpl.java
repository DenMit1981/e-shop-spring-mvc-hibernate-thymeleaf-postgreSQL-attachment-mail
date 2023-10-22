package com.training.eshop.service.impl;

import com.training.eshop.converter.AttachmentConverter;
import com.training.eshop.dao.AttachmentDAO;
import com.training.eshop.dto.AttachmentDto;
import com.training.eshop.model.Attachment;
import com.training.eshop.service.AttachmentService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private static final Logger LOGGER = LogManager.getLogger(AttachmentServiceImpl.class.getName());

    private static final String EMPTY_VALUE = "";
    private static final String ORDER_NOT_CREATED = "You didn't create an order";
    private static final String FILE_WITHOUT_NAME_ERROR = "fileWithoutNameError";
    private static final String FILE_NOT_SELECTED = "You should select the file first";
    private static final String ALL_FILES = "Your files";
    private static final String FILES_NOT_CHOSEN = "You can add files to your order";
    private static final String INVALID_FILE = "invalidFile";
    private static final String FILE_UPLOAD_ERROR = "fileUploadError";
    private static final String DOWNLOADABLE_FILE_FORMAT = "jpg|pdf|doc|docx|png|jpeg";
    private static final String DOWNLOADABLE_FILE_FORMAT_ERROR_MESSAGE = "The selected file type is not allowed. Please select a file of " +
            "one of the following types: pdf, png, doc, docx, jpg, jpeg.";
    private static final Double ALLOWED_MAXIMUM_SIZE = 5.0;
    private static final String ALLOWED_MAXIMUM_SIZE_ERROR_MESSAGE = "The size of the attached file should not be greater than 5 Mb. " +
            "Please select another file.";

    private final AttachmentDAO attachmentDAO;
    private final AttachmentConverter attachmentConverter;

    @Override
    @Transactional
    public List<AttachmentDto> getAllByOrderId(Long orderId) {
        List<Attachment> attachments = attachmentDAO.getAllByOrderId(orderId);

        LOGGER.info("All files for order {} : {}", orderId, attachments);

        return attachments.stream()
                .map(attachmentConverter::convertToAttachmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AttachmentDto getById(Long attachmentId, Long orderId) {
        Attachment attachment = attachmentDAO.getByAttachmentIdAndOrderId(attachmentId, orderId);

        return attachmentConverter.convertToAttachmentDto(attachment);
    }

    @Override
    @Transactional
    public void getEventsWithAttachment(AttachmentDto attachmentDto, String command, HttpSession session, Long orderId) {
        if (orderId != null) {
            saveOrRemoveAttachment(attachmentDto, command, session, orderId);
        } else {
            session.setAttribute("noOrderError", ORDER_NOT_CREATED);
        }
    }

    @Override
    public AttachmentDto getChosenAttachment(@NonNull MultipartFile file, HttpSession session, Long orderId) throws IOException {
        AttachmentDto attachmentDto = new AttachmentDto();

        String fileUploadError = validateUploadFile(file, orderId);

        if (!fileUploadError.isEmpty()) {
            session.setAttribute(FILE_UPLOAD_ERROR, fileUploadError);

            LOGGER.error(fileUploadError);

            attachmentDto.setName(INVALID_FILE);
        } else {
            attachmentDto.setName(file.getOriginalFilename());
            attachmentDto.setFile(file.getBytes());

            session.setAttribute(FILE_UPLOAD_ERROR, EMPTY_VALUE);
        }

        return attachmentDto;
    }

    @Override
    public String showChosenFiles(List<AttachmentDto> chosenFiles, Long orderId) {
        if (orderId != null) {
            if (!chosenFiles.isEmpty()) {
                return ALL_FILES;
            } else {
                return FILES_NOT_CHOSEN;
            }
        } else {
            return EMPTY_VALUE;
        }
    }

    @Transactional
    public void save(@NonNull AttachmentDto attachmentDto, Long orderId) {
        Attachment attachment = attachmentConverter.fromAttachmentDto(attachmentDto, orderId);

        attachmentDAO.save(attachment);

        LOGGER.info("New file {} has just been added to order {}", attachmentDto.getName(), orderId);
    }

    @Transactional
    public void deleteByName(String attachmentName, Long orderId) {
        attachmentDAO.deleteByAttachmentNameAndOrderId(attachmentName, orderId);

        LOGGER.info("File {} has just been deleted from order {}", attachmentName, orderId);
    }

    @Transactional
    public void saveOrRemoveAttachment(AttachmentDto attachmentDto, String command, HttpSession session, Long orderId) {
        String fileName = attachmentDto.getName();

        if (!fileName.equals(EMPTY_VALUE) && !fileName.equals(INVALID_FILE)) {
            clickingActionsWithAttachment(attachmentDto, command, orderId);

            session.setAttribute(FILE_WITHOUT_NAME_ERROR, EMPTY_VALUE);
        } else {
            getActionsIfFileIsInvalidOrNotSelected(attachmentDto, session, orderId);
        }
    }

    @Transactional
    public void clickingActionsWithAttachment(AttachmentDto attachmentDto, String command, Long orderId) {
        if (command.equals("Add File")) {
            save(attachmentDto, orderId);
        } else {
            if (command.equals("Delete File")) {
                deleteByName(attachmentDto.getName(), orderId);
            }
        }
    }

    private void getActionsIfFileIsInvalidOrNotSelected(AttachmentDto attachmentDto, HttpSession session, Long orderId) {
        if (orderId != null) {
            if (attachmentDto.getName().equals(EMPTY_VALUE)) {
                session.setAttribute(FILE_WITHOUT_NAME_ERROR, FILE_NOT_SELECTED);

                session.setAttribute(FILE_UPLOAD_ERROR, EMPTY_VALUE);
            } else {
                session.setAttribute(FILE_WITHOUT_NAME_ERROR, EMPTY_VALUE);
            }
        }
    }

    private String validateUploadFile(MultipartFile file, Long orderId) {
        String fileExtension = getFileExtension(file);

        if (file != null && orderId != null) {
            if (!fileExtension.matches(DOWNLOADABLE_FILE_FORMAT)
                    && !fileExtension.equals(EMPTY_VALUE)) {
                return DOWNLOADABLE_FILE_FORMAT_ERROR_MESSAGE;
            }

            if (getFileSizeMegaBytes(file) > ALLOWED_MAXIMUM_SIZE) {
                return ALLOWED_MAXIMUM_SIZE_ERROR_MESSAGE;
            }
        }
        return EMPTY_VALUE;
    }

    private String getFileExtension(MultipartFile file) {
        if (file == null) {
            return EMPTY_VALUE;
        }
        String fileName = file.getOriginalFilename();
        assert fileName != null;

        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return EMPTY_VALUE;
        }
    }

    private double getFileSizeMegaBytes(MultipartFile file) {
        return (double) file.getSize() / (1024 * 1024);
    }
}

