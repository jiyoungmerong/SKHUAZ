package com.app.skhuaz.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JoinRequest {

    private String email; // 이메일

    private String password; // 비밀번호

    private String nickname; // 닉네임

    private String semester; // 학기

    private boolean graduate; // 졸업 여부

    private String major1; // 전공 1

    private String major2; // 전공 2

    private boolean department; // 학부 여부

    private boolean major_minor; // 주-부전공 여부

    private boolean double_major; // 복수전공 여부
}