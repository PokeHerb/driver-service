package org.pokeherb.driverservice.driver.domain.application.dto;

import org.pokeherb.driverservice.driver.domain.entity.DriverType;

import java.util.UUID;

public record DriverCreateReqeustDto(

     Long hubId,
     UUID slackId,
     String name,
     DriverType driverType
) {
}
