package com.sunghyun.schedule.adapter.out.persistence.repository;

import com.sunghyun.schedule.adapter.out.persistence.entity.LocationEntity;
import com.sunghyun.schedule.adapter.out.persistence.entity.SchedulePollEntity;
import com.sunghyun.schedule.domain.Location;
import com.sunghyun.schedule.domain.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {
    private final SpringJpaLocationRepository springJpaLocationRepository;

    @Override
    public Location save(Location location) {
        return springJpaLocationRepository.save(LocationEntity.from(location))
                .toDomain()
                ;
    }

    @Override
    public Optional<Location> findByLocationNo(Long locationNo) {
        return springJpaLocationRepository.findById(locationNo)
                .map(LocationEntity::toDomain)
                ;
    }
}
