package com.app.skhuaz.response;

import com.app.skhuaz.domain.PreLecture;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserMainInformationResponse {
    private String nickname;

    private String major1;

    private String major2;

    public static UserMainInformationResponse of(String nickname, String major1, String major2){
        return new UserMainInformationResponse(nickname, major1, major2);
    }

}
