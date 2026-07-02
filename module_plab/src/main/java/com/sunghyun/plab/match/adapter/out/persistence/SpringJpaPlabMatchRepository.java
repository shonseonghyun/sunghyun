package com.sunghyun.plab.match.adapter.out.persistence;

import com.sunghyun.plab.match.domain.model.PlabMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringJpaPlabMatchRepository extends JpaRepository<PlabMatch,Long> {
    Optional<PlabMatch> findPlabMatchByPlabMatchNo(final Long plabMatchNo);
    List<PlabMatch> findByMatchDtBetween(final String startDt, final String endDt);
}
