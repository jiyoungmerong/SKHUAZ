package com.app.skhuaz.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Comparator;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class PrerequisitesResponse {

    private final Long preId;
    private final String subjectName;
    private final String subjectSemester;

    public static PrerequisitesResponse of(Long preId, String subjectName, String subjectSemester) {
        return new PrerequisitesResponse(preId, subjectName, subjectSemester);
    }

    public static Comparator<PrerequisitesResponse> semesterComparator = Comparator.comparing(o -> parseSemester(o.subjectSemester));

    private static int parseSemester(String semester) {

        return Integer.parseInt(semester.replace("학년 ", "").replace("학기", ""));
    }
}