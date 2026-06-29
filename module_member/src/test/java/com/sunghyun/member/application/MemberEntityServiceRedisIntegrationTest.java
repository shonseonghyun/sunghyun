package com.sunghyun.member.application;

import com.sunghyun.member.application.dto.res.MemberValidIdResDto;
import com.sunghyun.member.application.service.MemberService;
import com.sunghyun.member.domain.exception.PendingIdException;
import com.sunghyun.member.application.port.MemberIdPendingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//얜 reids만 활성화
//feign설정이 없어 에러
@SpringBootTest
@ActiveProfiles("test")
public class MemberEntityServiceRedisIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberIdPendingRepository memberIdPendingRepository;

    @Value("${member.valid-id.prefix}")
    private String pendingIdPrefix;

    @Value("${member.valid-id.timeout}")

    private Long timeout;
    private final static String ID = "test1234";

    //테스트가 끝날 때마다 Redis 데이터 청소
    @AfterEach
    void tearDown() {
        // 테스트가 끝날 때마다 깔끔하게 청소
        final String key = pendingIdPrefix+"*";
        memberIdPendingRepository.deleteAllPendingIds(key);
    }

    @Test
    @DisplayName("아이디 중복 검사 시 Redis에 선점 데이터가 생성되어야 한다")
    void validMemberId_success_and_save_redis() {
        // when
        final String key = pendingIdPrefix+ID;
        final MemberValidIdResDto memberValidIdResDto = memberService.validMemberId(ID);

        // then: 실제로 Redis에 데이터가 들어가 있는지 확인
        Object value = memberIdPendingRepository.getPendingValue(key);
        assertThat(value).isEqualTo(memberValidIdResDto.getPendingToken());

        // TTL(만료시간)이 설정되었는지 확인 (대략 300초 근처여야 함)
//        Long expire = redisTemplate.getExpire(expectedKey);
//        assertThat(expire).isGreaterThan(0);
    }

    @Test
    @DisplayName("이미 Redis에 선점된 아이디로 요청하면 PendingIdException 발생한다")
    void validMemberId_fail_due_to_redis_pending() {
        final String uuid= UUID.randomUUID().toString();
        // 첫 번째 요청 (선점 성공)
        memberService.validMemberId(ID);

        // when,then
        // 동일한 아이디로 두 번째 요청 (선점 실패)
        assertThatThrownBy(() -> memberService.validMemberId(ID))
                .isInstanceOf(PendingIdException.class);
    }
}
