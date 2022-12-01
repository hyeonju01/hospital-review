package com.example.hospitalreview2.controller;

import com.example.hospitalreview2.domain.Response;
import com.example.hospitalreview2.domain.dto.UserDto;
import com.example.hospitalreview2.domain.dto.UserJoinRequest;
import com.example.hospitalreview2.domain.dto.UserJoinResponse;
import com.example.hospitalreview2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/*
고관운님 위키 참고함 - https://likelion.notion.site/088264627b104146b6fd3e3d8b79f671
endpoint - http://localhost:8080/api/v1/users/join (talend api test로 해봐야 할 것 같다.)
 */
@RestController
@RequestMapping("/api/v1/users") //users로 오는 요청을 처리한다.
@RequiredArgsConstructor //생성자가 필요한 부분을 자동으로 생성
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest){
        UserDto userDto = userService.join(userJoinRequest);
        return Response.success(new UserJoinResponse(userDto.getUserName(), userDto.getEmail()));
    }
}