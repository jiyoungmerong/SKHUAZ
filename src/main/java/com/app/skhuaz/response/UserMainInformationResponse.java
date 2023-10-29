package com.app.skhuaz.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserMainInformationResponse {
    private String nickname;

    private String major1;

    private String major2;

    private String semester;

    private boolean graduate; // 졸업 여부

    private boolean department; // 학부 여부

    private boolean major_minor; // 주-부전공 여부

    private boolean double_major; // 복수전공 여부

    public static UserMainInformationResponse of(String nickname, String major1, String major2,
                                                 String semester, boolean graduate, boolean department,
                                                 boolean major_minor, boolean double_major){
        return new UserMainInformationResponse(nickname, major1, major2, semester, graduate, department, major_minor, double_major);
    }

}
