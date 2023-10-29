package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.PreLecture;
import com.app.skhuaz.service.PreLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PreLectureController {
    private final PreLectureService preLectureService;

    @PostMapping("/preLecture/create")
    public RspsTemplate<List<PreLecture>> addPreLectures(Principal principal) {
        return preLectureService.addLectures(principal.getName());
    }

    @GetMapping("/preLecture/check") // 사용자 선수과목 불러오기 => 루트평에서 사용
    public RspsTemplate<List<PreLecture>> getPreLectures(Principal principal) {
        return preLectureService.getUserPreLecture(principal.getName());
    }

}
