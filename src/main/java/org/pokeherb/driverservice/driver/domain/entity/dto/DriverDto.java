package org.pokeherb.driverservice.driver.domain.entity.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DriverDto(
        Long hubId,
        UUID slackId,
        String name,
        String driverType,
        String driverStatus
) {
}
