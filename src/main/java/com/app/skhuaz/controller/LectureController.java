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

    @PostMapping("/semesters/AllProfessor")
    public RspsTemplate<List<String>> getProfessorBySemesterAndLectureName(
            @RequestBody @Valid LectureFilterRequest request) {
        return lectureService.getLectureNameBySemesterAndProfessor(request.getSemester(), request.getLecName());
    }
}
