package org.pokeherb.driverservice.driver.domain.application.dto;

import java.util.UUID;

public record DriverIdDto(

        UUID driverId,
        String name

) {
    public static DriverIdDto of(UUID driverId, String name) {
        return new DriverIdDto(driverId, name);
    }
}
