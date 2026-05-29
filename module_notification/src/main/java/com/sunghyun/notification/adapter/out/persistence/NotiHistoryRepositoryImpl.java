package com.sunghyun.notification.adapter.out.persistence;

import com.sunghyun.notification.application.port.out.NotiHistoryRepository;
import com.sunghyun.notification.domain.model.NotiHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotiHistoryRepositoryImpl implements NotiHistoryRepository {
    private final SpringJpaNotiHistoryRepository springJpaNotiHistoryRepository;

    @Override
    public NotiHistory save(NotiHistory notiHistory) {
        return springJpaNotiHistoryRepository.save(NotiHistoryEntity.from(notiHistory)).toDomain();
    }

//    @Override
//    public List<NotiHistory> getNotiHistoriesByMemberNo(final Long memberNo) {
//        final List<NotiHistoryEntity> notiHistoryEntities = springJpaNotiHistoryRepository.findAllByMemberNo(memberNo);
//        return notiHistoryEntities.stream().map(NotiHistoryEntity::toDomain).toList();
//    }

    @Override
    public List<NotiHistory> findAll() {
        return springJpaNotiHistoryRepository.findAll()
                .stream()
                .map(NotiHistoryEntity::toDomain)
                .toList()
                ;
    }
}
