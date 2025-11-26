package org.pokeherb.driverservice.driver.domain.entity.dto;

import lombok.Builder;
import org.pokeherb.driverservice.driver.domain.entity.DriverStatus;
import org.pokeherb.driverservice.driver.domain.entity.DriverType;

import java.util.UUID;

@Builder
public record DriverDto(
        Long hubId,
        UUID slackId,
        String name,
        DriverType driverType,
        DriverStatus driverStatus
) {
}
