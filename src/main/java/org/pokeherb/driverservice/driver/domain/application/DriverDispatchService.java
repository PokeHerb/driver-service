package org.pokeherb.driverservice.driver.domain.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.driverservice.driver.domain.application.dto.DriverIdDto;
import org.pokeherb.driverservice.driver.domain.entity.Driver;
import org.pokeherb.driverservice.driver.domain.entity.DriverStatus;
import org.pokeherb.driverservice.driver.domain.entity.DriverType;
import org.pokeherb.driverservice.driver.domain.exception.DriverErrorCode;
import org.pokeherb.driverservice.driver.domain.infrastructure.DriverRepository;
import org.pokeherb.driverservice.global.infrastructure.exception.CustomException;
import org.pokeherb.driverservice.infrastructure.dto.DriverAssignedMessage;
import org.pokeherb.driverservice.infrastructure.rabbit.DriverEventProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverDispatchService {

    private final DriverRepository driverRepository;
    private final DriverEventProducer driverEventProducer;
    private final static int DRIVER_SIZE = 10;

    @Transactional
    public DriverIdDto dispatchVendorDriver(Long hubId, UUID orderId) {

        List<Driver> drivers = driverRepository.findAllByHubIdAndTypeWithLock(hubId, DriverType.VENDOR_DRIVER);

        return dispatchInternal(drivers, DriverType.VENDOR_DRIVER, hubId, orderId);
    }

    @Transactional
    public DriverIdDto dispatchHubDriver() {

        List<Driver> drivers = driverRepository.findAllByWithLock(DriverType.HUB_DRIVER);


        return dispatchInternal(drivers, DriverType.HUB_DRIVER, null, null);
    }

    private DriverIdDto dispatchInternal(List<Driver> drivers, DriverType type, Long hubId, UUID orderId) {

        if (drivers.isEmpty()) {
            throw new CustomException(DriverErrorCode.NO_AVAILABLE_DRIVER);
        }

        // 마지막으로 할당된 기사 찾는다 -> lastAssignedAt 기준으로 가장 최근 기사
        // 모두 null인 초기 상태라면, 0번 인덱스(sequence -> 1) 직전인 10번이 마지막이라고 가정
        int lastSequence = drivers.stream()
                .filter(driver -> driver.getLastAssignedAt() != null)
                .max(Comparator.comparing(Driver::getLastAssignedAt))
                .map(Driver::getSequence)
                .orElse(0);

        // 기사 할당 로직
        for (int i = 0; i < DRIVER_SIZE; i++) {

            int nextIndex = (lastSequence + i) % DRIVER_SIZE - 1;
            Driver candidate = drivers.get(nextIndex);

            if (candidate.getDriverStatus() == DriverStatus.WAITING) {
                candidate.startDelivery();
                log.info("[{}] 배차 성공 : 허브ID {}, 기사ID {}", type,  hubId, candidate.getId());

                if(orderId != null) {
                    DriverAssignedMessage message = DriverAssignedMessage.builder()
                            .orderId(orderId)
                            .deliveryStatus("ASSIGNED")
                            .changeAt(LocalDateTime.now())
                            .build();

                    driverEventProducer.sendDriverAssignedEvent(message);

                }
                return DriverIdDto.of(candidate.getId(), candidate.getName());
            }
        }



        return DriverIdDto.of(null, null);
    }
}
