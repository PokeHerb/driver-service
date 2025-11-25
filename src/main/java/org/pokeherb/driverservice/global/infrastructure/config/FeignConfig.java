package org.pokeherb.driverservice.global.infrastructure.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("org.pokeherb.driverservice.global.infrastructure.client")
public class FeignConfig {


}
