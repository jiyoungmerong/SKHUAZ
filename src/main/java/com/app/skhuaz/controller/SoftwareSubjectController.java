package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.SoftwareSubject;
import com.app.skhuaz.request.PreLectureEditRequest;
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

    @PostMapping("/add-preLecture") // admin 선수과목 추가
    public RspsTemplate<AddPreLectureResponse> addPrerequisiteLecture(@RequestBody PreLectureEditRequest request) {
        return softwareSubjectService.addPrerequisiteLecture(request);
    }

    @DeleteMapping("/delete/lecture/{preId}") // admin 선수과목 삭제
    public RspsTemplate<String> deletePrerequisiteLecture(@PathVariable Long preId) {
        return softwareSubjectService.deletePrerequisiteLecture(preId);
    }

    @PutMapping("/update/lecture/{preId}") // admin 선수과목 수정
    public RspsTemplate<AddPreLectureResponse> updatePrerequisiteLecture(@PathVariable Long preId,  @RequestBody PreLectureEditRequest request) {
        return softwareSubjectService.editPrerequisiteLecture(preId, request);
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

        return softwareSubjectService.updateCheckYn(subjectIds);

    }

    @GetMapping("/all-checkYn-change")
    public RspsTemplate<String> disableAllCheckYn() {
        return softwareSubjectService.disableAllCheckYn();
    }

}