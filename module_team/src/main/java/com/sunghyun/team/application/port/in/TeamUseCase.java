package com.sunghyun.team.application.port.in;

import com.sunghyun.team.application.port.in.dto.TeamCreateReqDto;
import com.sunghyun.team.application.port.out.dto.TeamCreateResDto;
import com.sunghyun.team.application.port.out.dto.TeamDetailInfoSelectResDto;
import com.sunghyun.team.application.port.out.dto.TeamSimpleInfoSelectResDto;

public interface TeamUseCase {
    TeamCreateResDto createTeam(Long creatorMemberNo, TeamCreateReqDto reqDto);
    void acceptMember(Long teamNo, Long newMemberNo);
    TeamDetailInfoSelectResDto getTeamDetailInfo(Long teamNo,Long memberNo);
    TeamDetailInfoSelectResDto getTeamDetailInfoByMemberNo(Long memberNo);
    TeamSimpleInfoSelectResDto getTeamSimpleInfo(Long teamNo);
}
