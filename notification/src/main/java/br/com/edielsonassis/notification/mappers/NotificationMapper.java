package br.com.edielsonassis.notification.mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;

import br.com.edielsonassis.notification.dtos.request.NotificationCommandRequest;
import br.com.edielsonassis.notification.models.NotificationModel;
import br.com.edielsonassis.notification.models.enums.NotificationStatus;

public class NotificationMapper {
    
    private static final String ZONE_ID = "America/Sao_Paulo";

    private NotificationMapper() {}

    public static NotificationModel toEntity(NotificationCommandRequest notificationRequest) {
        var notificationModel = new NotificationModel();
        BeanUtils.copyProperties(notificationRequest, notificationModel);
        notificationModel.setCreationDate(LocalDateTime.now(ZoneId.of(ZONE_ID)));
        notificationModel.setNotificationStatus(NotificationStatus.CREATED);
        return notificationModel;
    }

    public static NotificationModel toEntity(NotificationModel notificationModel) {
        notificationModel.setNotificationStatus(NotificationStatus.READ);
        return notificationModel;
    }
}