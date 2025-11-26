package org.pokeherb.driverservice.driver.domain.application.dto;

import org.pokeherb.driverservice.driver.domain.entity.DriverType;

public record DriverUpdateRequestDto(
        Long hubId,
        String name,
        DriverType driverType

) {
}
