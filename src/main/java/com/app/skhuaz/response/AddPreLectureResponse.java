package com.app.skhuaz.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AddPreLectureResponse {
    private String subjectName; // 과목명

    private String preLecName; // 선수과목

    public static AddPreLectureResponse of(String subjectName, String preLecName){
        return new AddPreLectureResponse(subjectName, preLecName);
    }

}
