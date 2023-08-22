package com.app.skhuaz.request;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateUserInformationRequest {
    private String nickname;

    private int semester;

    private boolean graduate; // 졸업 여부

    private String major1; // 전공 1

    private String major2; // 전공 2

    private boolean department; // 학부 여부

    private boolean major_minor; // 주-부전공 여부

    private boolean double_major; // 복수전공 여부

    public static UpdateUserInformationRequest of(String nickname, int semester, boolean graduate,
                                                  String major1, String major2, boolean department,
                                                  boolean major_minor, boolean double_major) {
        return new UpdateUserInformationRequest(nickname, semester, graduate, major1, major2,
                department, major_minor, double_major);
    }
}
