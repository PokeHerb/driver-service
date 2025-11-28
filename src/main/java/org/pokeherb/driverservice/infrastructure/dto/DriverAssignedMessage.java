package org.pokeherb.driverservice.infrastructure.dto;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DriverAssignedMessage(
        UUID orderId,
        String deliveryStatus,
        LocalDateTime changeAt
        )  {

}
