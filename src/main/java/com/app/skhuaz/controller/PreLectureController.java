package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.PreLecture;
import com.app.skhuaz.service.PreLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/preLecture")
public class PreLectureController {
    private final PreLectureService preLectureService;

    @PostMapping("/save")
    public RspsTemplate<List<PreLecture>> addLectures(@RequestBody List<PreLecture> lectures, Principal principal) {
        return preLectureService.addLectures(lectures, principal.getName());
    }

    @GetMapping("/preLectures")
    public RspsTemplate<List<PreLecture>> getPreLectures(Principal principal) {
        return preLectureService.getPreLecturesByEmail(principal.getName());
    }
}
