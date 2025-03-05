package br.com.edielsonassis.notification.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.edielsonassis.notification.models.enums.NotificationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = "notificationId")
@Getter
@Setter
@Entity
@Table(name = "notifications")
public class NotificationModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notification_id")
	private UUID notificationId;
	
	@Column(nullable = false, name = "user_id")
	private UUID userId;
	
	@Column(nullable = false, length = 150)
	private String title;
	
	@Column(nullable = false)
	private String message;
	
	@Column(nullable = false, name = "creation_date")
	private LocalDateTime creationDate;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "notification_status")
	private NotificationStatus notificationStatus;
}