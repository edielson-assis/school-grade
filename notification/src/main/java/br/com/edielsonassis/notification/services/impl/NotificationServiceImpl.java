package br.com.edielsonassis.notification.services.impl;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.edielsonassis.notification.dtos.request.NotificationCommandRequest;
import br.com.edielsonassis.notification.dtos.response.NotificationResponse;
import br.com.edielsonassis.notification.mappers.NotificationMapper;
import br.com.edielsonassis.notification.models.NotificationModel;
import br.com.edielsonassis.notification.models.enums.NotificationStatus;
import br.com.edielsonassis.notification.repositories.NotificationRepository;
import br.com.edielsonassis.notification.services.NotificationService;
import br.com.edielsonassis.notification.services.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    @Override
    public void saveNotification(NotificationCommandRequest notificationRequest) {
        var notification = NotificationMapper.toEntity(notificationRequest);
        log.info("Registering a new Notification: {}", notification.getTitle());
        notificationRepository.save(notification);
    }

    @Override
    public Page<NotificationResponse> findAllNotificationsByUser(UUID userId, Pageable pageable) {
        log.info("Listing all notifications by user: {}", userId);
        return notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.CREATED, pageable).map(notificationModel -> {
            var notificationResponse = new NotificationResponse();
            BeanUtils.copyProperties(notificationModel, notificationResponse);
            getFormattedEnumValue(notificationModel, notificationResponse);
            return notificationResponse;
        });
    }

    @Transactional
    @Override
    public NotificationResponse updateNotificationStatus(UUID notificationId, UUID userId) {
        var notification = findNotificationById(notificationId, userId);
        notification = NotificationMapper.toEntity(notification);
        log.info("Updating Notification with ID: {}", notification.getNotificationId());
        notificationRepository.save(notification);
        var notificationResponse = new NotificationResponse();
        BeanUtils.copyProperties(notification, notificationResponse);
        getFormattedEnumValue(notification, notificationResponse);
        return notificationResponse;
    }

    private void getFormattedEnumValue(NotificationModel notificationModel, NotificationResponse notificationResponse) {
        notificationResponse.setNotificationStatus(notificationModel.getNotificationStatus().getValue());
    }

    private NotificationModel findNotificationById(UUID notificationId, UUID userId) {
        log.info("Verifying notification by id: {}", notificationId);
        return notificationRepository.findByNotificationIdAndUserId(notificationId, userId).orElseThrow(() -> {
            log.error("Notification not found: {}", notificationId);
            return new ObjectNotFoundException("Notification not found: " + notificationId);
        });    
    }
}