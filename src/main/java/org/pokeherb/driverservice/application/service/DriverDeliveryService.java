package org.pokeherb.driverservice.application.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.driverservice.driver.domain.entity.Driver;
import org.pokeherb.driverservice.driver.domain.exception.DriverErrorCode;
import org.pokeherb.driverservice.driver.domain.infrastructure.DriverRepository;
import org.pokeherb.driverservice.global.infrastructure.exception.CustomException;
import org.pokeherb.driverservice.infrastructure.dto.DriverDeliveryStatus;
import org.pokeherb.driverservice.infrastructure.dto.DriverOrderStatus;
import org.pokeherb.driverservice.infrastructure.rabbit.DriverEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

// 배송 완료 처리 서비스
@Service
@RequiredArgsConstructor
public class DriverDeliveryService {

    private final DriverEventPublisher driverEventPublisher;
    private final DriverRepository driverRepository;

    @Transactional
    public void completeDelivery(UUID driverId, UUID orderId, UUID deliveryId) {

        // 배송 완료 이벤트 발행
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new CustomException(DriverErrorCode.DRIVER_NOT_FOUND));

        driver.endDelivery();

        DriverOrderStatus orderEvent = DriverOrderStatus.builder()
                .orderId(orderId)
                .orderStatus("DELIVERY_COMPLETED")
                .changedAt(LocalDateTime.now())
                .build();

        DriverDeliveryStatus deliveryEvent = DriverDeliveryStatus.builder()
                .deliveryId(deliveryId)
                .deliveryStatus("DELIVERY_COMPLETED")
                .changedAt(LocalDateTime.now())
                .build();

        driverEventPublisher.publishDeliveryCompletedToOrder(orderEvent);
        driverEventPublisher.publishDeliveryCompletedToDeliveryService(deliveryEvent);

    }

}
