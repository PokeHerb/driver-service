package org.pokeherb.driverservice.application.presentation;

import lombok.RequiredArgsConstructor;
import org.pokeherb.driverservice.application.service.DriverDeliveryService;
import org.pokeherb.driverservice.global.infrastructure.CustomResponse;
import org.pokeherb.driverservice.global.infrastructure.success.GeneralSuccessCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DriverDeliveryController {

    private final DriverDeliveryService driverDeliveryService;

    // 배송 완료 처리 API -> 배송 완료 이벤트 발행
    @GetMapping("/delivery/complete/{driverId}/{orderId}/{deliveryId}")
    public CustomResponse<?> completeDelivery(
            @PathVariable("driverId") UUID driverId,
            @PathVariable("orderId") UUID orderId,
            @PathVariable("deliveryId") UUID deliveryId
            ) {

        driverDeliveryService.completeDelivery(driverId, orderId, deliveryId);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK);
    }

}
