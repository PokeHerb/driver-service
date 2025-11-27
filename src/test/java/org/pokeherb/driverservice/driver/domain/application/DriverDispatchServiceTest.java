package org.pokeherb.driverservice.driver.domain.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.driverservice.driver.domain.application.dto.DriverIdDto;
import org.pokeherb.driverservice.driver.domain.entity.Driver;
import org.pokeherb.driverservice.driver.domain.entity.DriverType;
import org.pokeherb.driverservice.driver.domain.infrastructure.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DriverDispatchServiceTest {

    @Autowired
    private DriverDispatchService driverDispatchService;

    @Autowired
    private DriverRepository driverRepository;

    private final Long HUB_ID = 1L;
    private final DriverType TYPE = DriverType.HUB_DRIVER;

    @BeforeEach
    void setUp() {

        driverRepository.deleteAll();
        // 10명의 기사 생성 (Sequence 1 ~ 10)
        for (int i = 1; i <= 10; i++) {
            driverRepository.save(new Driver(HUB_ID, UUID.randomUUID(), DriverType.HUB_DRIVER, "Driver_" + i, i));

        }
    }

    @Test
    @DisplayName("순차적 배차(라운드 로빈) 테스트 - 1번부터 10번까지 순서대로 배정되어야 한다")
    void roundRobinTest() {
        for (int i = 1; i <= 10; i++) {
            DriverIdDto result = driverDispatchService.dispatchDriver(HUB_ID, TYPE);
            System.out.println("배차된 기사 ID: " + result.driverId());

            // 검증: 현재 배차된 기사의 ID를 조회하여 Sequence 확인
            Driver driver = driverRepository.findById(result.driverId()).orElseThrow();
            System.out.println("find DriverId: " + driver.getId() + ", Sequence: " + driver.getSequence());
            assertThat(driver.getSequence()).isEqualTo(i);
        }
    }

    @Test
    @DisplayName("동시성 테스트 - 5개의 스레드가 동시에 요청해도 서로 다른 기사를 배정받아야 한다")
    void concurrencyTest() throws InterruptedException {
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 성공 횟수 카운트
        AtomicInteger successCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    driverDispatchService.dispatchDriver(HUB_ID, TYPE);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // 5번의 요청이 모두 성공했는지 확인
        assertThat(successCount.get()).isEqualTo(5);

        // WAITING 상태인 기사는 5명이어야 함 (10명 중 5명 배차됨)
        long waitingCount = driverRepository.findAll().stream()
                .filter(d -> d.getDriverStatus().toString().equals("WAITING"))
                .count();
        assertThat(waitingCount).isEqualTo(5);
    }

}