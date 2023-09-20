package com.app.skhuaz.service;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.PreLecture;
import com.app.skhuaz.domain.Route;
import com.app.skhuaz.repository.PreLectureRepository;
import com.app.skhuaz.repository.UserRepository;
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
    private final UserRepository userRepository;

//    @Transactional
//    public void addLectures(List<PreLecture> lectures, String email) {
//        Optional<User> user = userRepository.findByEmail(email);
//
//        List<PreLecture> preLectures = new ArrayList<>(); // PreLecture 객체들을 저장할 리스트 생성
//
//        for (PreLecture lecture : lectures) {
//            String semester = lecture.getSemester();
//            List<String> lecNames = lecture.getLecNames();
//
//            for (String lecName : lecNames) {
//                PreLecture pre = PreLecture.builder()
//                        .semester(semester)
//                        .lecNames(lecNames) // lecName으로 변경
//                        .email(email) // 사용자 정보 설정
//                        .build();
//
//                preLectures.add(pre); // 리스트에 PreLecture 객체 추가
//            }
//        }
//
//        // preLectures 리스트에 저장된 PreLecture 객체들을 한 번에 저장
//        preLectureRepository.saveAll(preLectures);
//    }
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
        return new RspsTemplate<>(HttpStatus.OK, "선수과목 저장에 성공하였습니다.", userPreLectures);}

    public RspsTemplate<List<PreLecture>> getPreLecturesByEmail(String email) {
        List<PreLecture> preLectureList = preLectureRepository.findByEmail(email);
        return new RspsTemplate<>(HttpStatus.OK, "사용자 선수과목 목록 조회에 성공하였습니다.", preLectureList);
    }

    public List<PreLecture> getPreLecturesByRoute(Route route) {
        // Route 엔티티에서 preLectures 필드를 가져옵니다.
        List<PreLecture> preLectures = route.getPreLectures();

        // 가져온 preLectures 리스트를 반환합니다.
        return preLectures;
    }


}