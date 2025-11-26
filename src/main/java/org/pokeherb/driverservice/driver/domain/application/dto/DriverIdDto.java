package org.pokeherb.driverservice.driver.domain.application.dto;

import java.util.UUID;

public record DriverIdDto(

        UUID driverId

) {
    public static DriverIdDto of(UUID driverId) {
        return new DriverIdDto(driverId);
    }
}
