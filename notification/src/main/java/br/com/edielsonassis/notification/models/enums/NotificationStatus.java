package br.com.edielsonassis.notification.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationStatus {
    
	CREATED("Created"),
	READ("Read");

	private String value;
}