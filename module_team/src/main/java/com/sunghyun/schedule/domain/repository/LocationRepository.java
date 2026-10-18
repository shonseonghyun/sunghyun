package com.sunghyun.schedule.domain.repository;

import com.sunghyun.schedule.domain.Location;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface LocationRepository {
    Location save(Location location);
    Optional<Location> findByLocationNo(Long locationNo);
}
