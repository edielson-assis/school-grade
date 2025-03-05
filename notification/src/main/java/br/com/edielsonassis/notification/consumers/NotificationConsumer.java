package br.com.edielsonassis.notification.consumers;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.edielsonassis.notification.dtos.request.NotificationCommandRequest;
import br.com.edielsonassis.notification.services.NotificationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class NotificationConsumer {
    
    private final NotificationService notificationService;

    @RabbitListener(
		bindings = @QueueBinding(
			value = @Queue(value = "${schoolgrade.broker.queue.notificationCommandQueue.name}", durable = "true"),
			exchange = @Exchange(value = "${schoolgrade.broker.exchange.notificationCommandExchange}", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
			key = "${schoolgrade.broker.key.notificationCommandKey}"
		)
	)
	public void listen(@Payload NotificationCommandRequest notificationCommand) {
		notificationService.saveNotification(notificationCommand);
	}
}