package com.sunghyun.plab.match.adapter.out.persistence;

import com.sunghyun.plab.match.domain.model.PlabMatch;
import com.sunghyun.plab.match.application.port.out.repository.PlabMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlabMatchRepositoryImpl implements PlabMatchRepository {
    private final SpringJpaPlabMatchRepository springJpaPlabMatchRepository;

    @Override
    public PlabMatch save(final PlabMatch plabMatch) {
        return springJpaPlabMatchRepository.save(plabMatch);
    }

    @Override
    public Optional<PlabMatch> getPlabMatchByPlabMatchNo(final Long plabMatchNo) {
        return springJpaPlabMatchRepository.findPlabMatchByPlabMatchNo(plabMatchNo);
    }

    @Override
    public List<PlabMatch> getPlabMatches(String startDt, String endDt) {
        return springJpaPlabMatchRepository.findByMatchDtBetween(startDt,endDt);
    }

    @Override
    public List<PlabMatch> getPlabMatchesByDate(String targetDt) {
        return springJpaPlabMatchRepository.findByMatchDt(targetDt);
    }
}
