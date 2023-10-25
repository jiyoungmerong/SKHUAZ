package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.SoftwareSubject;
import com.app.skhuaz.request.AddPreLectureRequest;
import com.app.skhuaz.request.LectureFilterRequest;
import com.app.skhuaz.request.SavePreLecRequest;
import com.app.skhuaz.response.AddPreLectureResponse;
import com.app.skhuaz.response.PrerequisitesResponse;
import com.app.skhuaz.service.SoftwareSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/software")
public class SoftwareSubjectController {

    private final SoftwareSubjectService softwareSubjectService;

    @PostMapping("/selectLec") // 해당 학기에 관한 강의이름
    public RspsTemplate<List<SoftwareSubject>> getAvailableSubjectsBySemester(@RequestBody LectureFilterRequest request) {
        return softwareSubjectService.getSubjectsBySemester(request.getSemester());
    }

    @PostMapping("/add-preLecture") // 해당 학기에 수강 가능한 강의 목록 조회
    public RspsTemplate<AddPreLectureResponse> addPrerequisiteLecture(@RequestBody AddPreLectureRequest request) {
        return softwareSubjectService.addPrerequisiteLecture(request);
    }

    @GetMapping("/check/{subjectId}") // 선수과목 선택 가능 여부 판단
    public ResponseEntity<RspsTemplate<String>> checkEnrollment(@PathVariable Long subjectId) {
        return softwareSubjectService.canEnroll(subjectId);
    }

    @GetMapping("/all-prerequisites") // admin 선수과목 리스트 조회
    public RspsTemplate<List<PrerequisitesResponse>> getAllSubjectsNamesAndSemesters() {
        return softwareSubjectService.getAllPrerequisitesWithDetails();
    }

    @PostMapping("/updateCheckYn")
    public RspsTemplate<String> updateCheckYn(@RequestBody SavePreLecRequest request) {
        List<Long> subjectIds = request.getSubjectIds();
        // subjectIds를 이용한 로직 수행

        return softwareSubjectService.updateCheckYn(subjectIds);

    }

    @GetMapping("/all-checkYn-change")
    public RspsTemplate<String> disableAllCheckYn() {
        return softwareSubjectService.disableAllCheckYn();
    }

}