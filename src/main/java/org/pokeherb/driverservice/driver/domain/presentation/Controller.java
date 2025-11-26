package org.pokeherb.driverservice.driver.domain.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.pokeherb.driverservice.driver.domain.application.command.DriverCommandService;
import org.pokeherb.driverservice.driver.domain.application.dto.DriverCreateReqeustDto;
import org.pokeherb.driverservice.driver.domain.application.dto.DriverUpdateRequestDto;
import org.pokeherb.driverservice.driver.domain.application.query.DriverQueryService;
import org.pokeherb.driverservice.driver.domain.entity.dto.DriverDto;
import org.pokeherb.driverservice.global.infrastructure.CustomResponse;
import org.pokeherb.driverservice.global.infrastructure.success.GeneralSuccessCode;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Driver Controller", description = "Driver 관련 API")
public class Controller {

    private final DriverQueryService driverQueryService;
    private final DriverCommandService driverCommandService;

    @GetMapping("{driverId}")
    public CustomResponse<?> getDriver(@PathVariable("driverId") UUID driverId) {
        DriverDto driverDto = driverQueryService.getDriver(driverId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, driverDto);
    }

    @PostMapping("")
    public CustomResponse<?> createDriver(@RequestBody DriverCreateReqeustDto request) {

        DriverDto driverDto = driverCommandService.createDriver(request);
        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, driverDto);
    }

    @PutMapping("{driverId}")
    public CustomResponse<?> updateDriverType(
            @PathVariable("driverId") UUID driverId,
            @RequestBody DriverUpdateRequestDto requestDto
    ) {
        DriverDto driverDto = driverCommandService.updateDriver(driverId, requestDto);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, driverDto);
    }

    @DeleteMapping("{driverId}")
    public CustomResponse<?> deleteDriver(
            @PathVariable("driverId") UUID driverId) {

        driverCommandService.deleteDriver(driverId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK);
    }
}
