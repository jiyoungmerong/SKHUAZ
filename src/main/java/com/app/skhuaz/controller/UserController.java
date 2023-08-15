package com.app.skhuaz.controller;

import ch.qos.logback.core.status.ErrorStatus;
import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.User;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.jwt.dto.TokenDto;
import com.app.skhuaz.jwt.service.TokenManager;
import com.app.skhuaz.repository.UserRepository;
import com.app.skhuaz.request.JoinRequest;
import com.app.skhuaz.request.LoginRequest;
import com.app.skhuaz.response.JoinResponse;
import com.app.skhuaz.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join") // 회원가입
    public ResponseEntity<JoinResponse> join(@RequestBody @Valid final JoinRequest request){
        try {
            if (userService.checkDuplicateEmail(request.getEmail())) { // 이메일 중복시
                throw new BusinessException(ErrorCode.EMAIL_ALREADY_REGISTERED);
            } else if (userService.checkDuplicateNickname(request.getNickname())) { // 닉네임 중복시
                throw new BusinessException(ErrorCode.NICKNAME_ALREADY_REGISTERED);
            }

            JoinResponse response = userService.create(request);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) { // 그 밖의 예외 발생시
            // fixme 발생할 수 있는 다른 예외 처리
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login") // 로그인
    public ResponseEntity<TokenDto> login(@RequestBody @Valid final LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) { // 유저가 존재하지 않을 때
            throw new BusinessException(ErrorCode.USER_NOT_JOIN);
        }
        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) { // 비밀번호가 옳지 않을 때
            throw new BusinessException(ErrorCode.USER_CERTIFICATION_FAILED);
        }

        TokenDto tokenDto = userService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping("/checkDuplicate/{nickname}") // 닉네임 중복 확인
    public RspsTemplate<String> checkDuplicateNickname(@PathVariable final String nickname) {
        boolean isDuplicate = userService.checkDuplicateNickname(nickname);

        HttpStatus status = userService.checkDuplicateNickname(nickname) ? HttpStatus.CONFLICT : HttpStatus.OK;
        String message = isDuplicate ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다.";

        return new RspsTemplate<>(status, message);
    }


}