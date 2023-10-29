package com.app.skhuaz.response;

import com.app.skhuaz.domain.PreLecture;
import lombok.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
public class PreLectureResponse {
//    private Map<String, List<PreLectureInfoResponse>> semesters;
//
//    public PreLectureResponse(Map<String, List<PreLectureInfoResponse>> semesters) {
//        this.semesters = semesters;
//    }

    private Long preLectureId;
    private String semester;
    private List<String> lecNames;

//    private List<Map<String, Object>> preLectures;
//
//    public PreLectureResponse(List<Map<String, Object>> preLectures) {
//        this.preLectures = preLectures;
//    }

    @Builder
    public PreLectureResponse(Long preLectureId, String semester, List<String> lecNames) {
        this.preLectureId = preLectureId;
        this.semester = semester;
        this.lecNames = lecNames;
    }


}
