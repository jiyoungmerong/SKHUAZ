package com.app.skhuaz.response;

import com.app.skhuaz.domain.PreLecture;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PreLectureResponse {
    private Long preLectureId;
    private String semester;
    private List<String> lecNames;

    public static PreLectureResponse of(PreLecture preLecture) {
        return new PreLectureResponse(
                preLecture.getPreLectureId(),
                preLecture.getSemester(),
                preLecture.getLecNames()
        );
    }
}
