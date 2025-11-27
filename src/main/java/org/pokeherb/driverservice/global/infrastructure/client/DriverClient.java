package org.pokeherb.driverservice.global.infrastructure.client;

import org.pokeherb.driverservice.driver.domain.application.dto.DriverIdDto;
import org.pokeherb.driverservice.global.infrastructure.CustomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "driver-service")
public interface DriverClient {

    @GetMapping("/driver/vendor/{hubId}")
    CustomResponse<DriverIdDto> getDriver(@PathVariable("hubId") UUID hubId);

    @GetMapping("/driver/hub")
    CustomResponse<DriverIdDto> getHubDriverId();
}
