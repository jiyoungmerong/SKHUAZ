package com.app.skhuaz.service;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.PreLecture;
import com.app.skhuaz.repository.PreLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PreLectureService {
    private final PreLectureRepository preLectureRepository;

    @Transactional
    public RspsTemplate<List<PreLecture>> addLectures(List<PreLecture> lectures, String email) {
        for (PreLecture lecture : lectures) {
            lecture.addUser(email);
        }

        preLectureRepository.saveAll(lectures);
        return new RspsTemplate<>(HttpStatus.OK, "선수과목 저장에 성공하였습니다.", lectures);

    }

    public RspsTemplate<List<PreLecture>> getPreLecturesByUser(String userEmail) {
        // 사용자의 이메일을 기반으로 해당 사용자가 저장한 선수 과목 정보를 조회
        List<PreLecture> userPreLectures = preLectureRepository.findByEmail(userEmail);
        return new RspsTemplate<>(HttpStatus.OK, "선수과목 저장에 성공하였습니다.", userPreLectures);
    }

    @Transactional
    public List<PreLecture> getPreLecturesByEmail(String email) {
        List<PreLecture> preLectureList = preLectureRepository.findByEmail(email);
        // return new RspsTemplate<>(HttpStatus.OK, "사용자 선수과목 목록 조회에 성공하였습니다.", preLectureList);
        return preLectureList;
    }
}