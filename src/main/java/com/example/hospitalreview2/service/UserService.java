package com.example.hospitalreview2.service;

import com.example.hospitalreview2.domain.User;
import com.example.hospitalreview2.domain.dto.UserDto;
import com.example.hospitalreview2.domain.dto.UserJoinRequest;
import com.example.hospitalreview2.exception.ErrorCode;
import com.example.hospitalreview2.exception.HospitalReviewException;
import com.example.hospitalreview2.repository.UserRepository;
import com.example.hospitalreview2.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.secret}") //spring framework annotation.
    private String secretKey;

    //1000ms (1초 ) * 60 = 60초 (1분), 1분 * 60 = 60분 (1시간)
    private final long expiredTimeMs = 1000 * 60 * 60L; //1시간

    // 회원가입
    public UserDto join(UserJoinRequest request) {

        // userName 중복 여부를 확인한다.
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> {
                    throw new HospitalReviewException(ErrorCode.DUPLICATED_USER_NAME, String.format("Username:%s", request.getUserName()));
                });

        // 테이블에 저장한다.
        String password = encoder.encode(request.getPassword());
        User savedUser = userRepository.save(request.toEntity(password));

        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .password(savedUser.getPassword())
                .email(savedUser.getEmailAddress())
                .build();
    }

    public String login(String userName, String password) {
        // 기능 추가 예정
        // case 1 - userName 없음
        User selectedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewException(ErrorCode.USERNANE_NOT_FOUND, userName + "이 없습니다.."));
        // case 2 - password 틀림
        if(!encoder.matches(password, selectedUser.getPassword())) {
            throw new HospitalReviewException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다.");
        }

        String token = JwtTokenUtil.createToken(selectedUser.getUserName(), secretKey, expiredTimeMs);

        // case1, case2에서 Exception 발생하지 않으면 토큰 발행
        return token;
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewException(ErrorCode.USERNANE_NOT_FOUND, ""));
    }
}