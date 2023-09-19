package com.app.skhuaz.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RouteSaveRequest {
    private String title; // 제목

    private String recommendation; // 내용

    private Long preLectureId;
}
