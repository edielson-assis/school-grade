package br.com.edielsonassis.notification.dtos.request;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.edielsonassis.notification.dtos.views.NotificationView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest implements Serializable {

    @NotBlank(message = "Title is required", groups = {NotificationView.registrationPost.class})
    @JsonView(NotificationView.registrationPost.class)
    private String title;

    @NotBlank(message = "Message is required", groups = {NotificationView.registrationPost.class})
    @JsonView(NotificationView.registrationPost.class)
    private String message;

    @NotNull(message = "UserId is required", groups = {NotificationView.registrationPost.class})
    @JsonView(NotificationView.registrationPost.class)
    private UUID userId;
}