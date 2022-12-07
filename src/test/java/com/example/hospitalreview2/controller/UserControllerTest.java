package com.example.hospitalreview2.controller;

import com.example.hospitalreview2.domain.dto.UserDto;
import com.example.hospitalreview2.domain.dto.UserJoinRequest;
import com.example.hospitalreview2.domain.dto.UserLoginRequest;
import com.example.hospitalreview2.exception.ErrorCode;
import com.example.hospitalreview2.exception.HospitalReviewException;
import com.example.hospitalreview2.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest //mockTest 진행
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired //자바 object를 json으로 만들어주는 jackson 오브젝트이다.
    ObjectMapper objectMapper;

    @MockBean //UserService를 흉내낸다.
    UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join_success() throws Exception{
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("twohj1218")
                .password("password")
                .email("twohj1218@naver.com")
                .build();

        when(userService.join(any())).thenReturn(mock(UserDto.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest))) //값을 byte로 전송한다.
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패")
    @WithMockUser
    void join_fail() throws Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("twohj1218")
                .password("password")
                .email("twohj1218@naver.com")
                .build();

        when(userService.join(any())).thenThrow(new HospitalReviewException(ErrorCode.DUPLICATED_USER_NAME,""));
        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf()) //post 요청 시 넣어주어야 한다.
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isConflict());

    }

    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_success() throws Exception{
        String userName = "hyeonju";
        String password = "1q2w3e4r";

        when(userService.login(any(), any()))
                .thenReturn("token");

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 - userName 없음")
    @WithMockUser
    void login_fail1() throws Exception {
        String userName = "hyeonju";
        String password = "1q2w3e4r";

        when(userService.login(any(), any()))
                .thenThrow(new HospitalReviewException(ErrorCode.USERNANE_NOT_FOUND, ""));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - password 틀린 경우")
    @WithMockUser
    void login_fail2() throws Exception {
        String userName = "hyeonju";
        String password = "1q2w3e4r";

        when(userService.login(any(), any()))
                .thenThrow(new HospitalReviewException(ErrorCode.INVALID_PASSWORD, ""));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}