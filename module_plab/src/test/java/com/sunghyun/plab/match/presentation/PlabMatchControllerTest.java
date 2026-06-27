package com.sunghyun.plab.match.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.plab.match.application.port.in.PlabMatchRegisterFacadeUseCase;
import com.sunghyun.plab.match.application.port.in.PlabMatchUseCase;
import com.sunghyun.plab.match.adapter.in.web.PlabMatchController;
import com.sunghyun.web.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlabMatchController.class)
class PlabMatchControllerTest {
    @MockBean
    private PlabMatchRegisterFacadeUseCase plabMatchRegisterFacadeUseCase;

    @MockBean
    private PlabMatchUseCase plabMatchUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    private final String BASE_REG_URL = "/plab/match/";

    @Test
    @DisplayName("매치 번호에 숫자가 아닌 문자열이 들어오면 400 에러와 상세 메시지를 반환한다")
    void should_return_400_when_path_variable_is_not_numeric() throws Exception {
        // given
        final String invalidPlabMatchNo = "S";
        final String url = BASE_REG_URL+invalidPlabMatchNo;
//        final String url = "/plab/match/" + invalidPlabMatchNo;

        // when & then
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.F000.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.F000.getMessage()))
                .andExpect(jsonPath("$.detailMessages[0].field").value("plabMatchNo"))
                .andExpect(jsonPath("$.detailMessages[0].value").value(invalidPlabMatchNo));
    }

    @Test
    @DisplayName("매치 번호 경로가 누락되면 404 에러와 공통 404 에러 코드를 반환한다")
    void should_return_404_when_path_variable_is_missing() throws Exception {
        // when & then
        mockMvc.perform(post(BASE_REG_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.C404.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.C404.getMessage()));
    }

    @Test
    @DisplayName("지원하지 않는 HTTP Method(GET)로 요청하면 405 에러와 공통 405 에러 코드를 반환한다")
    void should_return_405_when_http_method_is_not_supported() throws Exception {
        // given
        final Long plabMatchNo = 1L;
        final String url = BASE_REG_URL + plabMatchNo;

        // when & then
        mockMvc.perform(get(url) // POST 전용 경로에 GET 요청
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code").value(ErrorCode.C405.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.C405.getMessage()));
    }
}