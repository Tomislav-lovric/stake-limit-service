package com.example.stake_limit_service.repository;

import com.example.stake_limit_service.entity.Device;
import com.example.stake_limit_service.entity.StakeLimit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class StakeLimitRepositoryTest {

    @Autowired
    private StakeLimitRepository stakeLimitRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @AfterEach
    void tearDown() {
        stakeLimitRepository.deleteAll();
    }

    @Test
    void StakeLimitRepository_existsByDevice_ReturnTrue() {
        // given
        Device device = new Device();
        deviceRepository.save(device);

        var stakeLimit = StakeLimit.builder()
                .device(device)
                .timeDuration(1800)
                .stakeLimit(999.0)
                .hotAmountPctg(80)
                .restrExpiry(300)
                .build();
        stakeLimitRepository.save(stakeLimit);

        // when
        boolean existsByDevice = stakeLimitRepository.existsByDevice(device);

        // then
        assertThat(existsByDevice).isTrue();
    }

    @Test
    void StakeLimitRepository_existsByDevice_ReturnFalse() {
        // given
        Device device = new Device();
        deviceRepository.save(device);

        // when
        boolean existsByDevice = stakeLimitRepository.existsByDevice(device);

        // then
        assertThat(existsByDevice).isFalse();
    }

    @Test
    void StakeLimitRepository_findByDevice_ReturnStakeLimit() {
        // given
        Device device = new Device();
        deviceRepository.save(device);

        var stakeLimit = StakeLimit.builder()
                .device(device)
                .timeDuration(1800)
                .stakeLimit(999.0)
                .hotAmountPctg(80)
                .restrExpiry(300)
                .build();
        stakeLimitRepository.save(stakeLimit);

        // when
        StakeLimit expected = stakeLimitRepository.findByDevice(device);

        // then
        assertThat(expected).isNotNull();
    }
}