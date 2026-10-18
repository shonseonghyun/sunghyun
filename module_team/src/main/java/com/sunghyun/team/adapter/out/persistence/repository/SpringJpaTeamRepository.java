package com.sunghyun.team.adapter.out.persistence.repository;

import com.sunghyun.team.adapter.out.persistence.entity.TeamEntity;
import com.sunghyun.team.adapter.out.persistence.entity.TeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringJpaTeamRepository extends JpaRepository<TeamEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(tm) > 0 THEN true ELSE false END " +
            "FROM TeamMemberEntity tm WHERE tm.realMemberNo = :memberNo")
    boolean existsJoinedTeamByMemberNo(@Param("memberNo") Long memberNo);

    @Query("SELECT t FROM TeamEntity t JOIN t.teamMemberList tm WHERE tm.realMemberNo = :memberNo")
    Optional<TeamEntity> findByMemberNo(@Param("memberNo") Long memberNo);

    // [신규 추가] teamNo와 realMemberNo로 teamMemberNo(PK) 조회
    @Query("SELECT tm.teamMemberNo FROM TeamEntity t JOIN t.teamMemberList tm " +
            "WHERE t.teamNo = :teamNo AND tm.realMemberNo = :memberNo")
    Long findTeamMemberNoByTeamNoAndRealMemberNo(
            @Param("teamNo") Long teamNo,
            @Param("memberNo") Long memberNo
    );

    @Query("SELECT tm FROM TeamEntity t JOIN t.teamMemberList tm WHERE t.teamNo = :teamNo")
    List<TeamMemberEntity> findTeamMembersByTeamNo(@Param("teamNo") Long teamNo);
}