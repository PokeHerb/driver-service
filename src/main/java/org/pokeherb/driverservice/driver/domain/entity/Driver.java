package org.pokeherb.driverservice.driver.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pokeherb.driverservice.driver.domain.application.dto.DriverUpdateRequestDto;
import org.pokeherb.driverservice.driver.domain.entity.dto.DriverDto;
import org.pokeherb.driverservice.driver.domain.exception.DriverErrorCode;
import org.pokeherb.driverservice.global.domain.Auditable;
import org.pokeherb.driverservice.global.infrastructure.exception.CustomException;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_driver")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Access(AccessType.FIELD)
public class Driver extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // 소속 허브 아이디 - 허브<->허브 배송 담당자는 허브 아이디가 필요 없음
    private Long hubId;

    // 슬랙 알림 아이디
    private UUID slackId;

    // 배송 순번(고정)
    private int sequence;

    @Enumerated(EnumType.STRING)
    private DriverType driverType;

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    private String name;

    // 마지막 배정 일시
    private LocalDateTime lastAssignedAt;

    @Builder
    public Driver(Long hubId, UUID slackId, DriverType driverType, String name, int sequence) {
        this.hubId = hubId;
        this.slackId = slackId;
        this.driverType = driverType;
        this.name = name;
        this.sequence = sequence;
        this.driverStatus = DriverStatus.WAITING;
        this.lastAssignedAt = null;
    }

    // 배송이 할당되어 시작된 경우
    public void startDelivery() {
        if (this.driverStatus != DriverStatus.WAITING) {
            throw new CustomException(DriverErrorCode.INVALID_DRIVER_STATUS);
        }
        this.driverStatus = DriverStatus.DRIVING;
        this.lastAssignedAt = LocalDateTime.now();
    }

    // 배송이 종료된 경우
    public void endDelivery() {
        this.driverStatus = DriverStatus.WAITING;
    }

    public void changeInfo(DriverUpdateRequestDto dto) {
        this.hubId = dto.hubId();
        this.name = dto.name();
        this.driverType = dto.driverType();
    }

    public DriverDto toDto() {
        return DriverDto.builder()
                .hubId(this.hubId)
                .slackId(this.slackId)
                .name(this.name)
                .driverType(this.driverType)
                .driverStatus(this.driverStatus)
                .build();
    }

    public void deleteDriver(String username) {
        softDelete(username);

    }
}
