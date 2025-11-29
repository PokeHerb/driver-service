package org.pokeherb.driverservice.infrastructure.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.driverservice.infrastructure.dto.DriverStatusCompleted;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitDriverProperties driverProperties;

    private static final String DELIVERY_COMPLETED_ROUTING_KEY = "driver.completed";

    public void publishDeliveryCompletedDelivery(DriverStatusCompleted event) {
        rabbitTemplate.convertAndSend(
                driverProperties.exchange(),
                DELIVERY_COMPLETED_ROUTING_KEY,
                event
        );

        log.info("배송 완료 이벤트 전송: {}", event);
    }

}


