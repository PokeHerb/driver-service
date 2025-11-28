package org.pokeherb.driverservice.infrastructure.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitDriverProperties.class)
public class RabbitConfig {

    private final RabbitDriverProperties driverProperties;

    // Driver 서비스는 큐를 직접 생성하지 않고, Exchange에 메시지를 던지기만 할 것
    // 받는 쪽(Order, Delivery)에서 각자의 큐를 생성하고 이 Exchange에 바인딩한다
    @Bean
    public TopicExchange driverExchange() {
        return new TopicExchange(driverProperties.exchange(), true, false);
    }

    // JSON 직렬화
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }


}
