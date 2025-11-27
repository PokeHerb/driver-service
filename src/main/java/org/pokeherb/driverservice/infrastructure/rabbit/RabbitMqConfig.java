package org.pokeherb.driverservice.infrastructure.rabbit;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    public static final String DRIVER_ORDER_STATUS_EXCHANGE = "order.dispatch.exchange";
    public static final String DRIVER_DELIVERY_STATUS_EXCHANGE = "delivery.dispatch.exchange";
//    public static final String DRIVER_ASSIGNED_ROUTING_KEY = "order.assigned";

    @Bean
    public TopicExchange driverOrderStatusExchange() {
        return new TopicExchange(DRIVER_ORDER_STATUS_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange driverDeliveryStatusExchange() {
        return new TopicExchange(DRIVER_DELIVERY_STATUS_EXCHANGE, true, false);
    }


}
