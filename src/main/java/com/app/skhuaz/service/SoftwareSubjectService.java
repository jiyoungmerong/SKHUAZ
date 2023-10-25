package com.app.skhuaz.service;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.Prerequisites;
import com.app.skhuaz.domain.SoftwareSubject;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.repository.PrerequisitesRepository;
import com.app.skhuaz.repository.SoftwareSubjectRepository;
import com.app.skhuaz.request.AddPreLectureRequest;
import com.app.skhuaz.response.AddPreLectureResponse;
import com.app.skhuaz.response.PrerequisitesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SoftwareSubjectService {

    private final SoftwareSubjectRepository softwareSubjectRepository;

    private final PrerequisitesRepository prerequisitesRepository;

    public RspsTemplate<List<SoftwareSubject>> getSubjectsBySemester(String semester) { // 학기 강의 목록 조
        List<SoftwareSubject> subjects = softwareSubjectRepository.findBySemesterLessThanEqualAndCheckYnIsFalseOrderBySemesterDesc(semester);
        if(subjects.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_EXISTS_LECNAME);

        }
        return new RspsTemplate<>(HttpStatus.OK, semester + "의 강의 목록 조회 성공!^^입니다", subjects);
    }

    public ResponseEntity<RspsTemplate<String>> canEnroll(Long subjectId) { // 선수과목 선택 가능 여부 판단
        Optional<SoftwareSubject> subject = softwareSubjectRepository.findById(subjectId);

        if (subject.isPresent()) {
            List<Prerequisites> prerequisitesList = prerequisitesRepository.findBySubjectId(subjectId);

            // 선수과목이 없으면 수강 가능
            if (prerequisitesList.isEmpty()) {
                return ResponseEntity.ok(new RspsTemplate<>(HttpStatus.OK, "수강 가능합니다.", subject.get().getSubjectName()));
            }

            // 수강할 수 없는 선수과목 찾기
            Optional<Prerequisites> uncompletedPrerequisite = prerequisitesList.stream()
                    .filter(prerequisite -> {
                        Optional<SoftwareSubject> prereqSubject = softwareSubjectRepository.findById(prerequisite.getPrerequisiteId());
                        return prereqSubject.isEmpty() || !prereqSubject.get().isCheckYn();
                    })
                    .findFirst();

            if (uncompletedPrerequisite.isPresent()) {
                SoftwareSubject prereqSubject = softwareSubjectRepository.findById(uncompletedPrerequisite.get().getPrerequisiteId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTS_LECTURE));
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new RspsTemplate<>(HttpStatus.ACCEPTED, "선수과목 '" + prereqSubject.getSubjectName() + "'을(를) 수강해야 합니다.", subject.get().getSubjectName()));
            }

            // 모든 선수과목의 checkYn이 true 면 수강 가능
            return ResponseEntity.ok(new RspsTemplate<>(HttpStatus.OK, "수강 가능합니다.", subject.get().getSubjectName()));
        }

        // 해당 강의가 존재하지 않는 경우 수강 불가능
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RspsTemplate<>(HttpStatus.NOT_FOUND, "해당 강의가 존재하지 않습니다.", null));
    }


    public RspsTemplate<List<PrerequisitesResponse>> getAllPrerequisitesWithDetails() { // admin 선수과목 리스트 조회
        Set<String> uniqueSubjectNames = new HashSet<>();
        List<PrerequisitesResponse> prerequisiteDetails = new ArrayList<>();

        List<Prerequisites> allPrerequisites = prerequisitesRepository.findAll();

        for (Prerequisites prerequisite : allPrerequisites) {
            SoftwareSubject subject = softwareSubjectRepository.findById(prerequisite.getSubjectId()).orElse(null);
            SoftwareSubject prerequisiteSubject = softwareSubjectRepository.findById(prerequisite.getPrerequisiteId()).orElse(null);

            if (subject != null) {
                String subjectName = subject.getSubjectName();
                if (uniqueSubjectNames.add(subjectName)) {
                    prerequisiteDetails.add(PrerequisitesResponse.of(subjectName, subject.getSemester()));
                }
            }

            if (prerequisiteSubject != null) {
                String preName = prerequisiteSubject.getSubjectName();
                if (uniqueSubjectNames.add(preName)) {
                    prerequisiteDetails.add(PrerequisitesResponse.from(preName, prerequisiteSubject.getSemester()));
                }
            }
        }

        prerequisiteDetails.sort(PrerequisitesResponse.semesterComparator);

        return new RspsTemplate<>(HttpStatus.OK, "admin용 선수과목 리스트 조회 성공입니다!!", prerequisiteDetails);
    }

    @Transactional
    public RspsTemplate<AddPreLectureResponse> addPrerequisiteLecture(AddPreLectureRequest request) { // admin 선수과목 추가
        SoftwareSubject subject = softwareSubjectRepository.findBySubjectNameAndSemester(request.getSubjectName(), request.getSemester());
        SoftwareSubject prerequisiteSubject = softwareSubjectRepository.findBySubjectNameAndSemester(request.getPreLecName(), request.getPreLecSemester());

        if (subject == null) {
            subject = SoftwareSubject.builder()
                    .subjectName(request.getSubjectName())
                    .semester(request.getSemester())
                    .checkYn(false)
                    .clickYn(false)
                    .build();
            softwareSubjectRepository.save(subject);
        }

        if (prerequisiteSubject == null) {
            prerequisiteSubject = SoftwareSubject.builder()
                    .subjectName(request.getPreLecName())
                    .semester(request.getPreLecSemester())
                    .checkYn(false)
                    .clickYn(false)
                    .build();
            softwareSubjectRepository.save(prerequisiteSubject);
        }

        Prerequisites prerequisites = Prerequisites.builder()
                .subjectId(subject.getSubjectId())
                .prerequisiteId(prerequisiteSubject.getSubjectId())
                .build();

        prerequisitesRepository.save(prerequisites);

        return new RspsTemplate<>(HttpStatus.OK, "admin용 선수과목 추가 성공!!", AddPreLectureResponse.of(request.getSubjectName(), request.getPreLecName()));

    }

    @Transactional
    public RspsTemplate<String> updateCheckYn(List<Long> subjectIds) { // checkYn 업데이트
        for (Long subjectId : subjectIds) {
            Optional<SoftwareSubject> optionalSubject = softwareSubjectRepository.findById(subjectId);

            if (optionalSubject.isPresent()) {
                SoftwareSubject subject = optionalSubject.get();
                subject.updateCheckYn(true);
                softwareSubjectRepository.save(subject);
            }
        }

        return new RspsTemplate<>(HttpStatus.OK, "강의의 checkYn이 성공적으로 변경되었습니다.", null);
    }

    @Transactional
    public RspsTemplate<String> disableAllCheckYn() {
        List<SoftwareSubject> softwareSubjects = softwareSubjectRepository.findAll();
        for (SoftwareSubject softwareSubject : softwareSubjects) {
            softwareSubject.updateCheckYn(false);
        }
        softwareSubjectRepository.saveAll(softwareSubjects);

        return new RspsTemplate<>(HttpStatus.OK, "강의의 checkYn이 성공적으로 변경되었습니다.", null);
    }
}