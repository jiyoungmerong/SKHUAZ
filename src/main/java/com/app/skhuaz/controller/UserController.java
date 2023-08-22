package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.User;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.exception.exceptions.NotValidTokenException;
import com.app.skhuaz.jwt.dto.TokenDto;
import com.app.skhuaz.jwt.service.TokenManager;
import com.app.skhuaz.repository.UserRepository;
import com.app.skhuaz.request.ChangePasswordRequest;
import com.app.skhuaz.request.JoinRequest;
import com.app.skhuaz.request.LoginRequest;
import com.app.skhuaz.request.UpdateUserInformationRequest;
import com.app.skhuaz.response.JoinResponse;
import com.app.skhuaz.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenManager tokenManager;

    @PostMapping("/join") // 회원가입
    public ResponseEntity<JoinResponse> join(@RequestBody @Valid final JoinRequest request) {

        JoinResponse response = userService.create(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login") // 로그인
    public ResponseEntity<TokenDto> login(@RequestBody @Valid final LoginRequest request) {

        TokenDto tokenDto = userService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(tokenDto);
    }

//    @GetMapping("/login-test") // accesstoken으로 유저정보 가져오기
//    public ResponseEntity<String> loginTest(@RequestHeader(value = "Authorization") String authHeader) {
//        try {
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                String accessToken = authHeader.substring(7); // "Bearer " 접두사 제거
//                if (tokenManager.validateToken(accessToken)) {
//                    return ResponseEntity.ok(tokenManager.getMemberEmail(accessToken));
//                } else {
//                    throw new BusinessException(ErrorCode.USER_CERTIFICATION_FAILED);
//                }
//            } else {
//                throw new BusinessException(ErrorCode.USER_CERTIFICATION_FAILED);
//            }
//        } catch (NotValidTokenException e) {
//            e.printStackTrace();
//            throw new BusinessException(ErrorCode.USER_CERTIFICATION_FAILED);
//        }
//    }

    @GetMapping("/checkDuplicate/{nickname}") // 닉네임 중복 체크
    public RspsTemplate<String> checkDuplicateNickname(@PathVariable final String nickname) {
        HttpStatus status = userService.checkDuplicateNickname(nickname) ? HttpStatus.CONFLICT : HttpStatus.OK;
        String message = status == HttpStatus.CONFLICT ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다.";

        return new RspsTemplate<>(status, message);
    }

    @PostMapping("/check/loginUser") // 탈퇴, 정보수정 시 로그인
    public ResponseEntity<RspsTemplate<String>> checkLogin(@RequestBody LoginRequest request, Principal principal) {
        String email = principal.getName(); // 로그인 정보에서 email 가져오기
        Optional<User> creator = userRepository.findByEmail(email); // 이메일에 해당하는 유저 가져오기

        if (creator.isEmpty() || !passwordEncoder.matches(request.getPassword(), creator.get().getPassword())
                || !email.equals(request.getEmail())) {
            throw new BusinessException(ErrorCode.MISMATCHED_SIGNIN_INFO);
        } else {
            RspsTemplate<String> response = new RspsTemplate<>(HttpStatus.OK, "인증 성공");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }


    @PostMapping("/myPage/password") // 비밀번호 수정
    public ResponseEntity<RspsTemplate<String>> changePassword(@RequestBody final ChangePasswordRequest request, Principal principal) {
        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_JOIN));

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            RspsTemplate<String> response = new RspsTemplate<>(HttpStatus.OK, "비밀번호 변경 성공");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            throw new BusinessException(ErrorCode.USER_CERTIFICATION_FAILED);
        }
    }

    @DeleteMapping("/delete") // 회원탈퇴
    public ResponseEntity<String> deleteUser(Principal principal) {
        try {
            String email = principal.getName();
            userService.deleteUser(Long.valueOf(email));
            return ResponseEntity.ok("탈퇴가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
        }
    }

    @PutMapping("/change-information") // 정보 수정
    public ResponseEntity<RspsTemplate<?>> changeUserInfo(@RequestBody @Valid final UpdateUserInformationRequest request,
                                                          Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_JOIN));

        userService.updateUserInformation(user, request);

        RspsTemplate<?> response = new RspsTemplate<>(HttpStatus.OK, request, "변경 성공");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

