package com.app.skhuaz.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EvaluationSaveResponse {
    private String deptName; // 학과 이름 (ex, 소프 컴공 ... )
    private String lecName; // 강의 이름
    private String profName; // 교수님
    private String semester; // 개설 학기
    private int teamPlay; // 팀플정도
    private int task; // 과제정도
    private int practice; // 실습정도
    private int presentation; // 발표 정도
    private String title;
    private String review; // 총평

    public static EvaluationSaveResponse of(String deptName, String lecName, String profName,
                                            String semester, int teamPlay, int task, int practice,
                                            int presentation, String title, String review){
        return new EvaluationSaveResponse(deptName, lecName, profName,
                semester, teamPlay, task, practice, presentation, title, review);
    }

}