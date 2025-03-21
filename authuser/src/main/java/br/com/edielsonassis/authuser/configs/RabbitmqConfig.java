package br.com.edielsonassis.authuser.configs;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class RabbitmqConfig {

    @Value(value = "${schoolgrade.broker.exchange.userEventExchange}")
	private String exchangeUserEvent;
    
	private final CachingConnectionFactory cachingConnectionFactory;
	
	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory);
		template.setMessageConverter(messageConverter());
		return template;
	}

	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return new Jackson2JsonMessageConverter(objectMapper);
	}
	
	@Bean
	public FanoutExchange fanoutUserEvent( ) {
		return new FanoutExchange(exchangeUserEvent);
	}
}