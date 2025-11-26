package org.pokeherb.driverservice.driver.domain.application.dto;

import org.pokeherb.driverservice.driver.domain.entity.DriverType;

import java.util.UUID;

public record DriverUpdateRequestDto(
        UUID driverId,
        Long hubId,
        String name,
        DriverType driverType

) {
}
