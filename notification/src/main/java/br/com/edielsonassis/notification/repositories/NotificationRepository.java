package br.com.edielsonassis.notification.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.edielsonassis.notification.models.NotificationModel;
import br.com.edielsonassis.notification.models.enums.NotificationStatus;

public interface NotificationRepository extends JpaRepository<NotificationModel, UUID> {
    
    Page<NotificationModel> findAllByUserIdAndNotificationStatus(UUID userId, NotificationStatus notificationStatus, Pageable pageable);

	Optional<NotificationModel> findByNotificationIdAndUserId(UUID notificationId, UUID userId);
}