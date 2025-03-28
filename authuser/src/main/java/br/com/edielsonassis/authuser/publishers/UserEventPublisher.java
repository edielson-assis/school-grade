package br.com.edielsonassis.authuser.publishers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.edielsonassis.authuser.dtos.request.UserEventRequest;
import br.com.edielsonassis.authuser.models.enums.ActionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserEventPublisher {

	@Value(value = "${schoolgrade.broker.exchange.userEventExchange}")
	private String exchangeUserEvent;
    
	private final RabbitTemplate rabbitTemplate; 
	
	public void publishUserEvent(UserEventRequest userEvent, ActionType actionType) {
		userEvent.setActionType(actionType.toString());
		rabbitTemplate.convertAndSend(exchangeUserEvent, "", userEvent);
	}
}