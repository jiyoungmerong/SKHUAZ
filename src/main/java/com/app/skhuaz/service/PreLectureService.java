package com.app.skhuaz.service;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.PreLecture;
import com.app.skhuaz.domain.SoftwareSubject;
import com.app.skhuaz.repository.PreLectureRepository;
import com.app.skhuaz.repository.RouteRepository;
import com.app.skhuaz.repository.SoftwareSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PreLectureService {
    private final PreLectureRepository preLectureRepository;

    private final SoftwareSubjectRepository softwareSubjectRepository;

    private final SoftwareSubjectService softwareSubjectService;

    private final RouteRepository routeRepository;

    @Transactional
    public RspsTemplate<List<PreLecture>> addLectures(String email) { //

        // checkYn이 true인 컬럼을 가져옴
        List<SoftwareSubject> softwareSubjects = softwareSubjectRepository.findByCheckYnTrue();

        Map<String, List<SoftwareSubject>> semesterMap = softwareSubjects.stream()
                .collect(Collectors.groupingBy(SoftwareSubject::getSemester));

        List<PreLecture> preLectures = new ArrayList<>();

        for (String semester : semesterMap.keySet()) {
            List<SoftwareSubject> subjectsPerSemester = semesterMap.get(semester);

            List<String> lecNames = subjectsPerSemester.stream()
                    .map(SoftwareSubject::getSubjectName)
                    .collect(Collectors.toList());

            PreLecture preLecture = PreLecture.builder()
                    .semester(semester)
                    .email(email)
                    .lecNames(lecNames)
                    .build();

            preLectures.add(preLecture);
        }

        preLectures.sort(Comparator.comparing(PreLecture::getSemester));

        preLectureRepository.saveAll(preLectures);

        softwareSubjectService.disableAllCheckYn();

        return new RspsTemplate<>(HttpStatus.OK, "선수과목 저장에 성공하였습니다.", preLectures);
    }



    public RspsTemplate<List<PreLecture>> getUserPreLecture(String email) {
        List<PreLecture> preLectures = preLectureRepository.findByEmail(email);

        // Route 테이블에서 preLectureId 리스트 가져오기
        List<Long> routePreLectureIds = routeRepository.findAll().stream()
                .flatMap(route -> route.getPreLectures().stream())
                .map(PreLecture::getPreLectureId).toList();

        // Route 테이블에 이미 존재하는 preLectureId면 제외
        preLectures = preLectures.stream()
                .filter(preLecture -> !routePreLectureIds.contains(preLecture.getPreLectureId()))
                .collect(Collectors.toList());

        Map<String, List<PreLecture>> semesterMap = preLectures.stream()
                .collect(Collectors.groupingBy(PreLecture::getSemester));

        List<PreLecture> combinedPreLectures = semesterMap.entrySet().stream()
                .map(entry -> {
                    String semester = entry.getKey();
                    List<PreLecture> preLecturesPerSemester = entry.getValue();

                    Long preLectureId = preLecturesPerSemester.get(0).getPreLectureId();

                    List<String> lecNames = preLecturesPerSemester.stream()
                            .flatMap(preLecture -> preLecture.getLecNames().stream())
                            .distinct()  // 중복 제거
                            .map(name -> {
                                if (name.startsWith("[") && name.endsWith("]")) {
                                    return name.substring(1, name.length() - 1);  // 대괄호를 제거
                                } else {
                                    return name;
                                }
                            })
                            .collect(Collectors.toList());


                    return PreLecture.builder()
                            .preLectureId(preLectureId)
                            .semester(semester)
                            .lecNames(lecNames)
                            .email(email)
                            .build();
                })
                .sorted((preLecture1, preLecture2) -> {
                    int year1 = Integer.parseInt(preLecture1.getSemester().substring(0, 1));
                    int semesterNum1 = preLecture1.getSemester().endsWith("1학기") ? 1 : 2;

                    int year2 = Integer.parseInt(preLecture2.getSemester().substring(0, 1));
                    int semesterNum2 = preLecture2.getSemester().endsWith("1학기") ? 1 : 2;

                    // 년도 비교 후 학기 비교
                    return year1 != year2 ? Integer.compare(year1, year2) : Integer.compare(semesterNum1, semesterNum2);
                })
                .collect(Collectors.toList());

        return new RspsTemplate<>(HttpStatus.OK, "사용자의 선수과목을 불러오는데 성공~~!!", combinedPreLectures);
    }


}