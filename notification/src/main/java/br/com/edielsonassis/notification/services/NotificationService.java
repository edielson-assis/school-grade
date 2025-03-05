package br.com.edielsonassis.notification.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.edielsonassis.notification.dtos.request.NotificationCommandRequest;
import br.com.edielsonassis.notification.dtos.request.NotificationRequest;
import br.com.edielsonassis.notification.dtos.response.NotificationResponse;

public interface NotificationService {

    void saveNotification(NotificationCommandRequest notificationRequest);

	Page<NotificationResponse> findAllNotificationsByUser(UUID userId, Pageable pageable);

	NotificationResponse updateNotification(UUID notificationId, UUID userId, NotificationRequest notificationRequest);
}