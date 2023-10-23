package com.app.skhuaz.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AddPreLectureRequest {
    private String subjectName; // 과목명

    private String semester; // 해당 과목의 학기

    private String preLecName; // 선수과목

    private String preLecSemester; // 선수과목의 학기

}
