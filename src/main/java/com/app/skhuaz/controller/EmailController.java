package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.request.EmailRequest;
import com.app.skhuaz.response.JoinResponse;
import com.app.skhuaz.service.EmailService;
import com.app.skhuaz.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/send") // 인증번호 발송
    public ResponseEntity<RspsTemplate<String>> sendEmail(@RequestBody EmailRequest emailRequest) throws Exception {
        try{
            emailService.sendJoinMessage(emailRequest.getEmail()); // 이메일로 인증코드 전송
            RspsTemplate<String> responseBody = new RspsTemplate<>(HttpStatus.OK, emailRequest.getEmail() + "로 이메일 인증 번호를 전송하였습니다.");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify/code") // 이메일 인증코드 검증
    public ResponseEntity<RspsTemplate<String>> verifyEmail(@RequestBody EmailRequest emailRequest) {
        if (emailVerificationService.verifyCode(emailRequest.getEmail(), emailRequest.getCode())) { // 인증 성공
            RspsTemplate<String> responseBody = new RspsTemplate<>(HttpStatus.OK, "인증에 성공하였습니다.");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } else {
            throw new BusinessException(ErrorCode.EMAIL_VERIFICATION_CODE_MISMATCHED);
        }
    }
}