package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.PreLecture;
import com.app.skhuaz.response.PrerequisitesResponse;
import com.app.skhuaz.service.PreLectureService;
import com.app.skhuaz.service.SoftwareSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PreLectureController {
    private final PreLectureService preLectureService;

    private final SoftwareSubjectService softwareSubjectService;

    @PostMapping("/preLecture/create") // 선수과목 저장
    public RspsTemplate<List<PreLecture>> addLectures(@RequestBody List<PreLecture> lectures, Principal principal) {
        return preLectureService.addLectures(lectures, principal.getName());
    }

    @GetMapping("/preLecture/check") // 로그인한 유저 선수과목 불러오기
    public RspsTemplate<List<PreLecture>> getPreLectures(Principal principal) {
         return new RspsTemplate<>(HttpStatus.OK, "사용자 선수과목 목록 조회에 성공하였습니다.", preLectureService.getPreLecturesByEmail(principal.getName()));
    }

}
