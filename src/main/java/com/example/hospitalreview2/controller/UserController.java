package com.example.hospitalreview2.controller;

import com.example.hospitalreview2.domain.Response;
import com.example.hospitalreview2.domain.dto.UserDto;
import com.example.hospitalreview2.domain.dto.UserJoinRequest;
import com.example.hospitalreview2.domain.dto.UserJoinResponse;
import com.example.hospitalreview2.domain.dto.UserLoginRequest;
import com.example.hospitalreview2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest dto) {
        String token = userService.login(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok().body(token);
    }
//    public ResponseEntity<String> login() {
//        return ResponseEntity.ok().body(userService.login("", ""));
//    }
}