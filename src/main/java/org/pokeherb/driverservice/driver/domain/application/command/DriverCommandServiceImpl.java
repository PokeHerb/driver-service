package org.pokeherb.driverservice.driver.domain.application.command;

import lombok.RequiredArgsConstructor;
import org.pokeherb.driverservice.driver.domain.application.dto.DriverCreateReqeustDto;
import org.pokeherb.driverservice.driver.domain.application.dto.DriverUpdateRequestDto;
import org.pokeherb.driverservice.driver.domain.entity.Driver;
import org.pokeherb.driverservice.driver.domain.entity.dto.DriverDto;
import org.pokeherb.driverservice.driver.domain.exception.DriverErrorCode;
import org.pokeherb.driverservice.driver.domain.infrastructure.DriverRepository;
import org.pokeherb.driverservice.global.infrastructure.exception.CustomException;
import org.pokeherb.driverservice.global.infrastructure.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DriverCommandServiceImpl implements DriverCommandService {

    private final DriverRepository driverRepository;
    private final SecurityUtils securityUtils;

    @Override
    public DriverDto createDriver(DriverCreateReqeustDto requestDto) {

        Driver newDriver = Driver.builder()
                .hubId(requestDto.hubId())
                .slackId(requestDto.slackId())
                .name(requestDto.name())
                .build();

        newDriver = driverRepository.save(newDriver);

        return newDriver.toDto();
    }

    @Override
    public DriverDto updateDriver(UUID driverId, DriverUpdateRequestDto requestDto) {

        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new CustomException(DriverErrorCode.DRIVER_NOT_FOUND));

        driver.changeInfo(requestDto);

        return driver.toDto();
    }

    @Override
    public void deleteDriver(UUID driverId) {

        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new CustomException(DriverErrorCode.DRIVER_NOT_FOUND));

        String username = securityUtils.getCurrentUsername();

        driver.deleteDriver(username);


    }
}
