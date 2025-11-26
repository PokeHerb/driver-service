package org.pokeherb.driverservice.driver.domain.application;

import lombok.RequiredArgsConstructor;
import org.pokeherb.driverservice.driver.domain.entity.Driver;
import org.pokeherb.driverservice.driver.domain.entity.DriverStatus;
import org.pokeherb.driverservice.driver.domain.entity.DriverType;
import org.pokeherb.driverservice.driver.domain.exception.DriverErrorCode;
import org.pokeherb.driverservice.driver.domain.infrastructure.DriverRepository;
import org.pokeherb.driverservice.global.infrastructure.exception.CustomException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverDispatchService {

    private final DriverRepository driverRepository;

    public Driver dispatchDriver(Long hubId, DriverType driverType) {

        int lastSequence = driverRepository.findLastAssignedDriver(hubId, driverType).map(Driver::getSequence).orElse(0);

        for (int i = 1; i <= 10; i++) {
            int nextSequence = ((lastSequence + i - 1) % 10) + 1;

            // 가능한 배송 담당자
            Driver candidate = driverRepository.findByHubIdAndSequenceWithLock(hubId, nextSequence, driverType).orElse(null);

            if (candidate != null && candidate.getDriverStatus() == DriverStatus.WAITING) {
                candidate.startDelivery();
                return candidate;
            }
        }

        throw new CustomException(DriverErrorCode.NO_AVAILABLE_DRIVER);
    }

}
