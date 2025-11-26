package org.pokeherb.driverservice.driver.domain.application.query;

import lombok.RequiredArgsConstructor;
import org.pokeherb.driverservice.driver.domain.entity.Driver;
import org.pokeherb.driverservice.driver.domain.entity.dto.DriverDto;
import org.pokeherb.driverservice.driver.domain.exception.DriverErrorCode;
import org.pokeherb.driverservice.driver.domain.infrastructure.DriverRepository;
import org.pokeherb.driverservice.global.infrastructure.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DriverQueryServiceImpl implements DriverQueryService {

    private final DriverRepository driverRepository;

    @Override
    public DriverDto getDriver(UUID driverId) {


        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new CustomException(DriverErrorCode.DRIVER_NOT_FOUND));

        return driver.toDto();

    }
}
