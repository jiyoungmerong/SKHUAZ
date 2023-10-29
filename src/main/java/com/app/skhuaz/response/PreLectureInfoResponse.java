package com.app.skhuaz.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class PreLectureInfoResponse {
    private Long preLectureId;
    private List<String> lecNames;

    public PreLectureInfoResponse(Long preLectureId, List<String> lecNames) {
        this.preLectureId = preLectureId;
        this.lecNames = lecNames;
    }
}
