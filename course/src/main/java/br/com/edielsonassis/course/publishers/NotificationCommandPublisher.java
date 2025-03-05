package br.com.edielsonassis.course.publishers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.edielsonassis.course.dtos.request.NotificationCommandRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class NotificationCommandPublisher {

	private final RabbitTemplate rabbitTemplate;
	
	@Value(value = "${schoolgrade.broker.exchange.notificationCommandExchange}")
	private String notificationCommandExchange;
	
	@Value(value = "${schoolgrade.broker.key.notificationCommandKey}")
	private String notificationCommandKey;
	
	public void publishNotificationCommand(NotificationCommandRequest notificationCommand) {
		rabbitTemplate.convertAndSend(notificationCommandExchange, notificationCommandKey, notificationCommand);
	}
}