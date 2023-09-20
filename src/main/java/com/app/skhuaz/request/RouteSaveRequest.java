package com.app.skhuaz.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RouteSaveRequest {
    private String title; // 제목

    private String recommendation; // 내용

    private List<Long> preLectureList; // 선수과목(PreLecture)의 ID 리스트

}
