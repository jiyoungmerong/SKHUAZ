package com.app.skhuaz.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 이메일
    EMAIL_VERIFICATION_CODE_MISMATCHED(400, "이메일 인증에 실패하였습니다."),
    EMAIL_ALREADY_REGISTERED(409, "이미 가입된 이메일입니다."),
    EMAIL_CANNOT_BE_SENT(500, "이메일을 보낼 수 없습니다."),
    EMAIL_VERIFICATION_CODE_NOT_FOUND(400, "이메일 인증 코드가 존재하지 않습니다."),

    // 닉네임
    NICKNAME_ALREADY_REGISTERED(409, "이미 존재하는 닉네임입니다."),

    // 인증 - 로그인 시도
    MISMATCHED_SIGNIN_INFO(400, "로그인 정보가 옳지 않습니다."),
    USER_CERTIFICATION_FAILED(401, "비밀번호가 옳지 않습니다."),
    USER_NOT_JOIN(401, "해당 이메일을 가진 유저가 존재하지 않습니다."),
    STATUS_NOT_LOGIN(401, "로그인 되어있지 않습니다."),

    // 인증 - 토큰
    NOT_EXISTS_AUTHORIZATION(401, "Authorization Header가 빈 값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE(401, "인증 타입이 Bearer 타입이 아닙니다."),
    ACCESS_TOKEN_EXPIRED(401, "해당 access token은 만료됐습니다."),
    NOT_ACCESS_TOKEN_TYPE(401, "tokenType이 access token이 아닙니다."),
    REFRESH_TOKEN_EXPIRED(401, "해당 refresh token은 만료됐습니다."),
    REFRESH_TOKEN_NOT_FOUND(400, "해당 refresh token은 존재하지 않습니다."),
    NOT_VALID_TOKEN(401, "유효하지 않은 토큰입니다."),

    // 서버 에러
    INTERNAL_SERVER_ERROR(500, "서버 문제 발생");

    private int statusCode;
    private String message;

    ErrorCode(int status, String message) {
        this.statusCode = status;
        this.message = message;
    }

}