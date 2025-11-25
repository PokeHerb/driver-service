package org.pokeherb.driverservice.global.infrastructure.error;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    HttpStatus getStatus();
    String getCode();
    String getMessage();

}

