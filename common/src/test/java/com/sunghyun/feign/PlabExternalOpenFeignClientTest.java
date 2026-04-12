package com.sunghyun.feign;

import com.sunghyun.exception.ExternalResourceNotFoundException;
import com.sunghyun.feign.dto.PlabMatchResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(
        classes = {OpenFeignConfig.class},
        properties = {
                "plab.api.url=https://www.plabfootball.com/api/v2/matches"
        }
)
@EnableAutoConfiguration
class PlabExternalOpenFeignClientTest {
    @Autowired
    private PlabExternalOpenFeignClient openFeignClient;

    @Test
    @DisplayName("존재하는 플랩 매치 번호로 플랩 API 호출 시 조회된다.")
    void fetchRealPlabMatchWithExistPlabMatchNo(){
        //given
        final Long targetMatchNo = 798646L;

        //when
        PlabMatchResponseDto result = openFeignClient.getMatch(targetMatchNo);

        //then
        assertThat(result.getId()).isEqualTo(targetMatchNo);
    }

    @Test
    @DisplayName("존재하지 않는 플랩 매치 번호로 플랩 API 호출 시 ExternalResourceNotFoundException 던진다")
    void fetchRealPlabMatchWithNotExistPlabMatchNo(){
        //given
        final Long targetMatchNo = 155555555L;

        //when,then
        assertThatThrownBy(()->openFeignClient.getMatch(targetMatchNo))
                .isInstanceOf(ExternalResourceNotFoundException.class)
                ;

    }

}