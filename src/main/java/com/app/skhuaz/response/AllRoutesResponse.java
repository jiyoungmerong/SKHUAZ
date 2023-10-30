package com.app.skhuaz.response;

import com.app.skhuaz.domain.PreLecture;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AllRoutesResponse {
    private Long routeId; // 루트 ID 추가
    private String title;
    private String recommendation;
    private LocalDateTime createAt;
    private String email;
    private String nickname;
    private List<PreLecture> preLectures;

    public static AllRoutesResponse of(Long routeId, String title, String recommendation,
                                       LocalDateTime createAt, String email,
                                       String nickname, List<PreLecture> preLectures){
        return new AllRoutesResponse(routeId, title, recommendation, createAt, email, nickname, preLectures);
    }
}
