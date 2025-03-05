package br.com.edielsonassis.notification.dtos.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.edielsonassis.notification.dtos.views.NotificationView;
import br.com.edielsonassis.notification.models.enums.NotificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationUpdateRequest implements Serializable {
    
    @NotNull(message = "NotificationStatus is required", groups = {NotificationView.notificationPut.class})
    @JsonView(NotificationView.notificationPut.class)
    private NotificationStatus notificationStatus;
}