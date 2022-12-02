package com.example.hospitalreview2.service;

import com.example.hospitalreview2.domain.User;
import com.example.hospitalreview2.domain.dto.UserDto;
import com.example.hospitalreview2.domain.dto.UserJoinRequest;
import com.example.hospitalreview2.exception.ErrorCode;
import com.example.hospitalreview2.exception.HospitalReviewException;
import com.example.hospitalreview2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
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
}