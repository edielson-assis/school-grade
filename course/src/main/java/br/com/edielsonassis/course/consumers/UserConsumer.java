package br.com.edielsonassis.course.consumers;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.edielsonassis.course.dtos.request.UserEventRequest;
import br.com.edielsonassis.course.mappers.UserMapper;
import br.com.edielsonassis.course.models.enums.ActionType;
import br.com.edielsonassis.course.services.UserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class UserConsumer {

	private final UserService userService;
	
	@RabbitListener(
		bindings = @QueueBinding(
			value = @Queue(value = "${schoolgrade.broker.queue.userEventQueue.name}", durable = "true"),
			exchange = @Exchange(name = "${schoolgrade.broker.exchange.userEventExchange}", type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true")
		)
	)
	public void listenUserEvent(@Payload UserEventRequest userEvent) {
		var userModel = UserMapper.toDto(userEvent);
		
		switch (ActionType.valueOf(userEvent.getActionType())) {
			case CREATE:
			case UPDATE:
				userService.saveUser(userModel);
				break;
			case DELETE:
				userService.deleteUserById(userModel.getUserId());
				break;
		}
	}
}