package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.User;
import com.app.skhuaz.jwt.dto.TokenDto;
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
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

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

    @GetMapping("/checkDuplicate/{nickname}") // 닉네임 중복 체크
    public RspsTemplate checkDuplicateNickname(@PathVariable final String nickname) {
        return userService.checkDuplicateNickname(nickname);
    }

    @PostMapping("/check/loginUser") // 탈퇴, 정보수정 시 로그인
    public RspsTemplate checkLogin(@RequestBody LoginRequest request, Principal principal) {
        return userService.checkUser(request, principal);
    }

    @PostMapping("/myPage/password") // 비밀번호 수정
    public RspsTemplate<String> changePassword(@RequestBody final ChangePasswordRequest request, Principal principal) {
        return userService.changePassword(request, principal);
    }

    @DeleteMapping("/delete") // 회원 탈퇴
    public ResponseEntity<RspsTemplate<String>> deleteUser(Principal principal) {
        Optional<User> user = userRepository.findByEmail(principal.getName());

        try{
            userRepository.delete(user.get());
            return ResponseEntity.ok(new RspsTemplate<>(HttpStatus.OK, principal.getName(), "탈퇴에 성공하였습니다."));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RspsTemplate<>(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 발생"));

        }
    }

    @PutMapping("/changeUser") // 정보 수정
    public RspsTemplate<String> changeUserInfo(@RequestBody @Valid final UpdateUserInformationRequest request,
                                                          Principal principal){
        return userService.updateUserInformation(request, principal);
    }
}