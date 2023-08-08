package com.app.skhuaz.response;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JoinResponse {
    private String email; // 이메일

    private String nickname; // 닉네임

    private String semester; // 학기

    private boolean graduate; // 졸업 여부

    private String major1; // 전공 1

    private String major2; // 전공 2

    private boolean department; // 학부 여부

    private boolean major_minor; // 주-부전공 여부

    private boolean double_major; // 복수전공 여부

    public static JoinResponse of(String email, String nickname, String semester,
                                  boolean graduate, String major1, String major2,
                                  boolean department, boolean major_minor, boolean double_major){
        return new JoinResponse(email, nickname, semester, graduate,
                major1, major2, department, major_minor, double_major);
    }

}
