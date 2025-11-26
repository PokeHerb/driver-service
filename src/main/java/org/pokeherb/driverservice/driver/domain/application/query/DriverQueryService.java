package org.pokeherb.driverservice.driver.domain.application.query;

import org.pokeherb.driverservice.driver.domain.entity.dto.DriverDto;

import java.util.UUID;

public interface DriverQueryService {

    DriverDto getDriver(UUID driverId);
}
