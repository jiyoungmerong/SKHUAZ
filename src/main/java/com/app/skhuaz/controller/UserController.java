package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.jwt.dto.TokenDto;
import com.app.skhuaz.request.ChangePasswordRequest;
import com.app.skhuaz.request.JoinRequest;
import com.app.skhuaz.request.LoginRequest;
import com.app.skhuaz.request.UpdateUserInformationRequest;
import com.app.skhuaz.response.JoinResponse;
import com.app.skhuaz.service.UserService;
import com.app.skhuaz.util.PrincipalUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/join") // 회원가입
    public RspsTemplate<JoinResponse> join(@RequestBody @Valid final JoinRequest request) {
        return userService.create(request);
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
        return userService.checkUser(request, principal.getName());
    }

    @PutMapping("/dhormrjEmsmswlahfmrpTsmsep") // 비밀번호 수정
    public ResponseEntity<String> updatePassword(@RequestBody @Valid ChangePasswordRequest request, Principal principal) {
        try {
            userService.changePassword(request, principal.getName());
            return ResponseEntity.ok("Password updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update password: " + e.getMessage());
        }
    }

//    @PostMapping("changePassword") // 비밀번호 수정
//    public RspsTemplate<String> changePassword(@RequestBody final ChangePasswordRequest request, Principal principal) {
//        return userService.changePassword(request, principal);
//    }

    @DeleteMapping("/delete") // 회원 탈퇴
    public RspsTemplate<String> deleteUser(Principal principal) {
        return userService.deleteUser(principal);
    }

    @PostMapping("/changeUser") // 정보 수정
    public RspsTemplate<String> changeUserInfo(@RequestBody @Valid final UpdateUserInformationRequest request,
                                                          Principal principal){
        return userService.updateUserInformation(request, principal.getName());
    }

    // 로그아웃
    @PostMapping("/logout")
    public RspsTemplate<Void> logout(Principal principal) {
        // 액세스 토큰 검증은 필터에서 거치므로 바로 로그아웃 처리
        userService.logout(PrincipalUtil.toEmail(principal));

        return new RspsTemplate<>(HttpStatus.OK, "로그아웃 성공");
    }

    // 비밀번호 찾기
}