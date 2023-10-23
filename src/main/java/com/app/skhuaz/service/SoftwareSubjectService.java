package com.app.skhuaz.service;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.Prerequisites;
import com.app.skhuaz.domain.SoftwareSubject;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.exception.exceptions.PrerequisiteNotSatisfiedException;
import com.app.skhuaz.repository.PreLectureRepository;
import com.app.skhuaz.repository.PrerequisitesRepository;
import com.app.skhuaz.repository.SoftwareSubjectRepository;
import com.app.skhuaz.request.AddPreLectureRequest;
import com.app.skhuaz.request.SubjectCheckRequest;
import com.app.skhuaz.response.AddPreLectureResponse;
import com.app.skhuaz.response.JoinResponse;
import com.app.skhuaz.response.PrerequisitesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SoftwareSubjectService {

    private final SoftwareSubjectRepository softwareSubjectRepository;

    private final PrerequisitesRepository prerequisitesRepository;

    public RspsTemplate<List<SoftwareSubject>> getSubjectsBySemester(String semester) {
        List<SoftwareSubject> subjects = softwareSubjectRepository.findBySemesterLessThanEqualAndCheckYnIsFalseOrderBySemesterDesc(semester);
        if(subjects.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_EXISTS_LECNAME);

        }
        return new RspsTemplate<>(HttpStatus.OK, semester + "의 강의 목록 조회 성공!^^입니다", subjects);
    }

//    @Transactional
//    public void selectSoftwares(SubjectCheckRequest request) {
//        List<BusinessException> exceptions = new ArrayList<>();
//
//        for (String lecName : request.getLecNames()) {
//            Optional<SoftwareSubject> subjectOptional = softwareSubjectRepository.findBySubjectName(lecName); // 강의 이름 가져오기
//            if (!subjectOptional.isPresent()) {
//                exceptions.add(new BusinessException(ErrorCode.NOT_EXISTS_LECTURE)); // 강의 존재 X
//                continue;
//            }
//
//            SoftwareSubject subject = subjectOptional.get();
//
//            // 해당 강의에 대한 선수과목 Id 가져오기
//            List<Prerequisites> prerequisites = prerequisitesRepository.findBySubject_SubjectId(subject.getSubjectId());
//
//            for (Prerequisites prerequisite : prerequisites){
//                SoftwareSubject preSubject = prerequisite.getPrerequisite();
//                Long preSubjectId = preSubject.getSubjectId();
//
//                Optional<SoftwareSubject> optionalPreSubj = softwareSubjectRepository.findBySubjectId(preSubjectId);
//
//                if(optionalPreSubj.isPresent()) {
//                    SoftwareSubject actualPreSubj = optionalPreSubj.get();
//                    if(!actualPreSubj.isCheckYn()){
//                        exceptions.add(new BusinessException(ErrorCode.PREREQUISITE_NOT_SATISFIED, actualPreSubj.getSubjectName()));
//                    }
//                } else {
//                    exceptions.add(new BusinessException(ErrorCode.SUBJECT_NOT_FOUND, preSubjectId));
//                }
//            }
//
//            subject.updateCheckYn();
//        }
//
//        if (!exceptions.isEmpty()) {
//            String message = exceptions.stream()
//                    .map(BusinessException::getMessage)
//                    .collect(Collectors.joining(", "));
//            throw new BusinessException(ErrorCode.MULTIPLE_ERRORS_OCCURRED, message);
//        }
//
//
//        softwareSubjectRepository.saveAll(request.getLecNames()
//                .stream()
//                .map(lecName -> softwareSubjectRepository.findBySubjectName(lecName).get())
//                .collect(Collectors.toList()));
//    }

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
}