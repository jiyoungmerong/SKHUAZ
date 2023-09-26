package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.request.LectureFilterRequest;
import com.app.skhuaz.service.LectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lecture")
public class LectureController {
    private final LectureService lectureService;

    @PostMapping("/semesters") // 학기 목록 조회
    public RspsTemplate<Set<String>> getAllSemesters() {
        return lectureService.getAllSemesters();
    }

    @PostMapping("/semesters/AllLecture")
    public RspsTemplate<List<String>> getLecturesBySemester(@RequestBody @Valid LectureFilterRequest request) {
        return lectureService.getProfessorsBySemester(request.getSemester());
    }

//    @GetMapping("/check/profName") // 해당 학기의 교수님 이름 조회
//    public RspsTemplate<List<String>> getProfessorsBySemester(@RequestBody @Valid LectureFilterRequest request) {
//        return lectureService.getProfessorsBySemester(request.getSemester());
//    }

//    @GetMapping("/check/lecName") // 학기와 교수님 이름으로 강의이름목록 조회
//    public RspsTemplate<List<String>> getLectureNameBySemesterAndProfessor(@RequestBody @Valid LectureFilterRequest request) {
//        return lectureService.getLectureNameBySemesterAndProfessor(request.getSemester(), request.getProfName());
//    }

    @PostMapping("/semesters/AllProfessor")
    public RspsTemplate<List<String>> getProfessorBySemesterAndLectureName(
            @RequestBody @Valid LectureFilterRequest request) {
        return lectureService.getLectureNameBySemesterAndProfessor(request.getSemester(), request.getLecName());
    }
}
