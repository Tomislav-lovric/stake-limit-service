package com.example.stake_limit_service.repository;

import com.example.stake_limit_service.entity.Device;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class DeviceRepositoryTest {

    @Autowired
    private DeviceRepository deviceRepository;

    @AfterEach
    void tearDown() {
        deviceRepository.deleteAll();
    }

    @Test
    void testFindDeviceByIdShouldReturnDevice() {
        // given
        Device device = new Device();
        deviceRepository.save(device);

        // when
        Device deviceById = deviceRepository.findDeviceById(device.getId());

        // then
        assertThat(deviceById).isNotNull();

    }
}