package org.pokeherb.driverservice.driver.domain.infrastructure;

import jakarta.persistence.LockModeType;
import org.pokeherb.driverservice.driver.domain.entity.Driver;
import org.pokeherb.driverservice.driver.domain.entity.DriverType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    // 마지막이 할당된 배송기사 조회
    @Query("""
            SELECT d
            FROM Driver d
            WHERE d.hubId = :hubId
            AND d.driverType = :type
            ORDER BY d.lastAssignedAt DESC
            LIMIT 1
            """)
    Optional<Driver> findLastAssignedDriver(@Param("hubId") Long hubId, @Param("type") DriverType driverType);

    // 특정 순번의 배송기사 가져오기
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT d
            FROM Driver d
            WHERE d.hubId = :hubId
            AND d.sequence = :sequence
            AND d.driverType = :type
            """)
    Optional<Driver> findByHubIdAndSequenceWithLock(@Param("hubId") Long hubId, @Param("sequence")int sequence, @Param("type") DriverType driverType);
}
