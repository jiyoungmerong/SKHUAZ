package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.Prerequisites;
import com.app.skhuaz.domain.SoftwareSubject;
import com.app.skhuaz.exception.exceptions.PrerequisiteNotSatisfiedException;
import com.app.skhuaz.request.AddPreLectureRequest;
import com.app.skhuaz.request.LectureFilterRequest;
import com.app.skhuaz.request.SubjectCheckRequest;
import com.app.skhuaz.response.AddPreLectureResponse;
import com.app.skhuaz.service.SoftwareSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/software")
public class SoftwareSubjectController {

    private final SoftwareSubjectService softwareSubjectService;


    @GetMapping("/selectLec") // 해당 학기에 관한 강의이름
    public RspsTemplate<List<SoftwareSubject>> getAvailableSubjectsBySemester(@RequestBody LectureFilterRequest request) {
        return softwareSubjectService.getSubjectsBySemester(request.getSemester());
    }

    @PostMapping("/add-preLecture")
    public RspsTemplate<AddPreLectureResponse> addPrerequisiteLecture(@RequestBody AddPreLectureRequest request) {
        return softwareSubjectService.addPrerequisiteLecture(request);
    }

//    @PostMapping("/select")
//    public ResponseEntity<?> selectSoftware(@RequestBody SubjectCheckRequest request) throws PrerequisiteNotSatisfiedException {
//        softwareSubjectService.selectSoftwares(request);
//        return ResponseEntity.ok().build();
//    }


}