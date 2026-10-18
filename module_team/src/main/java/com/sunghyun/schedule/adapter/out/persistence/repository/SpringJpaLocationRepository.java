package com.sunghyun.schedule.adapter.out.persistence.repository;

import com.sunghyun.schedule.adapter.out.persistence.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaLocationRepository extends JpaRepository<LocationEntity,Long> {

}
