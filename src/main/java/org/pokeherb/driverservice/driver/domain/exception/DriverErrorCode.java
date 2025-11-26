package org.pokeherb.driverservice.driver.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pokeherb.driverservice.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DriverErrorCode implements BaseErrorCode {

    DRIVER_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "배송자를 찾을 수 없습니다."),
    INVALID_DRIVER_STATUS(HttpStatus.BAD_REQUEST, "400", "배송 가능한 상태가 아닙니다."),
    NO_AVAILABLE_DRIVER(HttpStatus.CONFLICT, "409", "사용 가능한 배송자가 없습니다. (모든 기사 배송 중)");

    private final HttpStatus status;
    private final String code;
    private final String message;

    }
