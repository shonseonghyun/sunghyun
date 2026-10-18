package com.sunghyun.team.application.port.in;

public interface TeamRequestUseCase {
    void requestTeamJoin(Long teamNo, Long memberNo);
    void acceptRequest(Long requestNo, Long memberNo);
    void rejectRequest(Long requestNo, Long memberNo);
    void cancelRequest(Long requestNo, Long memberNo);
}

