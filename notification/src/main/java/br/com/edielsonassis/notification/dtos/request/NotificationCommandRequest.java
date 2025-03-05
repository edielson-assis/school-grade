package br.com.edielsonassis.notification.dtos.request;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationCommandRequest implements Serializable {

    private String title;
    private String message;
    private UUID userId;
}