package com.example.stake_limit_service.repository;

import com.example.stake_limit_service.entity.Device;
import com.example.stake_limit_service.entity.StakeLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StakeLimitRepository extends JpaRepository<StakeLimit, UUID> {

    boolean existsByDevice(Device device);
    StakeLimit findByDevice(Device device);

}
