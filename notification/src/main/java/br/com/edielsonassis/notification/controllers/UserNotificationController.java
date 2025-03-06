package br.com.edielsonassis.notification.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.edielsonassis.notification.dtos.response.NotificationResponse;
import br.com.edielsonassis.notification.services.NotificationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserNotificationController {
    
    private final NotificationService notificationService;

    @GetMapping("/users/{userId}/notifications")
	public ResponseEntity<Page<NotificationResponse>> getAllNotificationByUser(
            @PathVariable UUID userId, 
            @PageableDefault(page = 0, size = 10, sort = "notificationId", direction = Sort.Direction.ASC) Pageable pageable) {
		var notifications = notificationService.findAllNotificationsByUser(userId, pageable);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
	}

    @PatchMapping("/users/{userId}/notifications/{notificationId}")
    public ResponseEntity<NotificationResponse> updateNotificationStatus(
            @PathVariable UUID userId,
            @PathVariable UUID notificationId){
        var notification = notificationService.updateNotificationStatus(notificationId, userId);
        return new ResponseEntity<>(notification, HttpStatus.OK);
    }
}