package org.pokeherb.driverservice.infrastructure.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DriverDeliveryStatus(
        UUID deliveryId,
        String deliveryStatus,
        LocalDateTime changedAt
) {
}
