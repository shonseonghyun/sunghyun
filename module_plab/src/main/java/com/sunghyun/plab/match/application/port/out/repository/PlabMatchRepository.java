package com.sunghyun.plab.match.application.port.out.repository;

import com.sunghyun.plab.match.domain.model.PlabMatch;

import java.util.List;
import java.util.Optional;

public interface PlabMatchRepository {
    PlabMatch save(final PlabMatch plabMatch);
    Optional<PlabMatch> getPlabMatchByPlabMatchNo(final Long plabMatchNo);
    List<PlabMatch> getPlabMatches(String startDt, String endDt);
}
