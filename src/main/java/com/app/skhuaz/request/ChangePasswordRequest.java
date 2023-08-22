package com.app.skhuaz.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChangePasswordRequest {
    private String password; // 현재 비밀번호

    private String newPassword; // 변경될 비밀번호
}
