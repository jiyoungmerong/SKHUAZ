package com.app.skhuaz.service;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.PreLecture;
import com.app.skhuaz.domain.SoftwareSubject;
import com.app.skhuaz.repository.PreLectureRepository;
import com.app.skhuaz.repository.SoftwareSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PreLectureService {
    private final PreLectureRepository preLectureRepository;

    private final SoftwareSubjectRepository softwareSubjectRepository;

    @Transactional
    public RspsTemplate<List<PreLecture>> addLectures(String email) {

        // checkYn이 true인 컬럼을 가져옴
        List<SoftwareSubject> softwareSubjects = softwareSubjectRepository.findByCheckYnTrue();

        List<PreLecture> preLectures = new ArrayList<>();

        for (SoftwareSubject softwareSubject : softwareSubjects) {
            PreLecture preLecture = PreLecture.builder()
                    .semester(softwareSubject.getSemester())
                    .email(email)
                    .lecNames(Collections.singletonList(softwareSubject.getSubjectName()))
                    .build();

            preLectures.add(preLecture);
        }

        preLectureRepository.saveAll(preLectures);
        return new RspsTemplate<>(HttpStatus.OK, "선수과목 저장에 성공하였습니다.", preLectures);
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