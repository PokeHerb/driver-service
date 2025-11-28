package org.pokeherb.driverservice.infrastructure.rabbit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbit.driver")
public record RabbitDriverProperties(
        String exchange,
        String queue,
        String routingKey
) {


}
