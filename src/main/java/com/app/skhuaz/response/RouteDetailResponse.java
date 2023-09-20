package com.app.skhuaz.response;

import com.app.skhuaz.domain.PreLecture;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RouteDetailResponse {
    private String title; // 루트 글의 제목
    private String recommendation; // 루트 글의 내용
    private List<PreLecture> preLectures; // 연관된 선수과목 정보

    public static RouteDetailResponse of(String title, String recommendation, List<PreLecture> preLectures){
        return new RouteDetailResponse(title, recommendation, preLectures);
    }
}
