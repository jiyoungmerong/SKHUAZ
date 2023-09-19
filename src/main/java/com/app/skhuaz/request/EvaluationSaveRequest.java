package com.app.skhuaz.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EvaluationSaveRequest {
    @NotBlank(message = "학과 이름을 필수 입력 항목입니다.")
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
    private String email;
}