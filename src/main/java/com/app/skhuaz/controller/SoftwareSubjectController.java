package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.SoftwareSubject;
import com.app.skhuaz.request.LectureFilterRequest;
import com.app.skhuaz.service.SoftwareSubjectService;
import lombok.RequiredArgsConstructor;
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


}
