package org.pokeherb.driverservice.infrastructure.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.driverservice.infrastructure.dto.DriverAssignedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


/**
 * 배차가 완료되었음을 알리는 메시지를 발행하는 클래스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DriverAssignedProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publish(DriverAssignedMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.DRIVER_ORDER_STATUS_EXCHANGE,
                message
        );

        log.info("RabbitMQ 기사 배정되었다는 메시지 발행: orderId={}",
                message.orderId());
    }

}
