package org.pokeherb.driverservice.driver.domain.application.command;

import org.pokeherb.driverservice.driver.domain.application.dto.DriverCreateReqeustDto;
import org.pokeherb.driverservice.driver.domain.application.dto.DriverUpdateRequestDto;
import org.pokeherb.driverservice.driver.domain.entity.dto.DriverDto;

import java.util.UUID;

public interface DriverCommandService {


    DriverDto createDriver(DriverCreateReqeustDto requestDto);

    DriverDto updateDriver(UUID driverId, DriverUpdateRequestDto requestDto);

    void deleteDriver(String username, UUID driverId);


}
