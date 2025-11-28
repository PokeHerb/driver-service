package org.pokeherb.driverservice.infrastructure.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.driverservice.infrastructure.dto.DriverAssignedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverEventProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitDriverProperties driverProperties;

    private static final String ROUTING_KEY_ASSIGNED = "order.status";

    public void sendDriverAssignedEvent(DriverAssignedMessage message) {

        log.info("드라이버 할당: OrderId = {}, DeliveryStatus = {}", message.orderId(), message.deliveryStatus());

        rabbitTemplate.convertAndSend(
                driverProperties.exchange(),
                ROUTING_KEY_ASSIGNED,
                message
        );
    }
}
