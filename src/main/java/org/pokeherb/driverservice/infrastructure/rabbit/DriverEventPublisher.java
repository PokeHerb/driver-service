package org.pokeherb.driverservice.infrastructure.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.driverservice.infrastructure.dto.DriverDeliveryStatus;
import org.pokeherb.driverservice.infrastructure.dto.DriverOrderStatus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitDriverProperties driverProperties;

    public void publishDeliveryCompletedToOrder(DriverOrderStatus event) {
        rabbitTemplate.convertAndSend(
                driverProperties.exchange(),
                RabbitConfig.RK_DELIVERY_COMPLETED_ORDER,
                event
        );

        log.info("Order-Service에 배송 완료 이벤트 전송: {}", event);
    }

    public void publishDeliveryCompletedToDeliveryService(DriverDeliveryStatus event) {
        rabbitTemplate.convertAndSend(
                driverProperties.exchange(),
                RabbitConfig.RK_DELIVERY_COMPLETED_DELIVERY,
                event
        );

        log.info("Delivery-Service에 배송 완료 이벤트 전송: {}", event);
    }
}

