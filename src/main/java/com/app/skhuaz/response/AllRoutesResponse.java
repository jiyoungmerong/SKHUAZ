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
    private String title;
    private String recommendation;
    private LocalDateTime createAt;
    private String email;
    private List<PreLecture> preLectures;

    public static AllRoutesResponse of(String title, String recommendation, LocalDateTime createAt, String email, List<PreLecture> preLectures){
        return new AllRoutesResponse(title, recommendation, createAt, email, preLectures);
    }
}
