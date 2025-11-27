package org.pokeherb.driverservice.infrastructure.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitDriverProperties.class)
public class RabbitConfig {

    private final RabbitDriverProperties driverProperties;
    private final static String DLX = "driver.dlx";
    private final static String DLQ = "driver.dlq";

    // order, delivery 로 보낼 routing key 예시
    public static final String RK_DELIVERY_COMPLETED_ORDER   = "driver.delivery.completed.order";
    public static final String RK_DELIVERY_COMPLETED_DELIVERY = "driver.delivery.completed.delivery";


    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 리스너 컨테이너 설정
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);

        // 예외 발생 시 다시 같은 큐가 아닌, DLXf로 보내도록 false
        factory.setDefaultRequeueRejected(false);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;

    }

   /* @Bean
    public TopicExchange driverExchange() {
        return new TopicExchange(driverProperties.exchange(), true, false);
    }*/

    @Bean
    public TopicExchange driverExchange() {
        return ExchangeBuilder
                .topicExchange(driverProperties.exchange())
                .durable(true)
                .build();
    }

    @Bean
    public Queue driverQueue() {
        return QueueBuilder
                .durable(driverProperties.queue())
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", DLQ)
                .build();
    }

    @Bean
    public Queue driverDeadLetterQueue() {
        return QueueBuilder
                .durable(DLQ)
                .build();
    }

    @Bean
    public TopicExchange driverDlxExchange() {
        return ExchangeBuilder
                .topicExchange(DLX)
                .durable(true)
                .build();
    }

    @Bean
    public Binding driverQueueBinding(Queue driverQueue, TopicExchange driverExchange) {
        return BindingBuilder
                .bind(driverQueue)
                .to(driverExchange)
                .with(driverProperties.routingKey());
    }

    @Bean
    public Binding driverDeadLetterBinding(Queue driverDeadLetterQueue, TopicExchange driverDlxExchange) {
        return BindingBuilder
                .bind(driverDeadLetterQueue)
                .to(driverDlxExchange)
                .with(DLQ);
    }

}