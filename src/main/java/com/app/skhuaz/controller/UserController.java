package com.app.skhuaz.controller;

import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.request.JoinRequest;
import com.app.skhuaz.response.JoinResponse;
import com.app.skhuaz.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/join") // 회원가입
    public ResponseEntity<JoinResponse> join(@RequestBody @Valid JoinRequest request){
        try {
            if (userService.checkDuplicateEmail(request.getEmail())) { // 이메일 중복시
                throw new BusinessException(ErrorCode.EMAIL_ALREADY_REGISTERED);
            } else if (userService.checkDuplicateNickname(request.getNickname())) { // 닉네임 중복시
                throw new BusinessException(ErrorCode.NICKNAME_ALREADY_REGISTERED);
            }

            JoinResponse response = userService.create(request);

            // body에 어떤게 들어가는지 확실히 파악할 수 있게
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) { // 그 밖의 예외 발생시
            // fixme 발생할 수 있는 다른 예외 처리
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}