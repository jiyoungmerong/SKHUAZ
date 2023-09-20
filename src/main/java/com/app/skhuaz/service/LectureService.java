package com.app.skhuaz.service;


import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {
    private final LectureRepository lectureRepository;

    public RspsTemplate<Set<String>> getAllSemesters() {
        List<String> allSemesters = lectureRepository.findAllSemesters();
        Set<String> uniqueSemesters = new HashSet<>(allSemesters);
        return new RspsTemplate<>(HttpStatus.OK, "학기 목록 조회 성공!^^입니다", uniqueSemesters);
    }


    public RspsTemplate<List<String>> getProfessorsBySemester(String semester) {
        List<String> professors = lectureRepository.findProfessorsBySemester(semester);
        if(professors.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_EXISTS_PROFNAME);
        }
        return new RspsTemplate<>(HttpStatus.OK, semester + "의 교수님 목록 조회 성공!^^입니다", professors);
    }

    public RspsTemplate<List<String>> getLectureNameBySemesterAndProfessor(String semester, String professorName) {
        List<String> LecNames = lectureRepository.findLectureNameBySemesterAndProfessor(semester, professorName);
        if(LecNames.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_EXISTS_LECNAME);

        }
        return new RspsTemplate<>(HttpStatus.OK, semester + "의 " + professorName + "교수님 강의 목록 조회 성공!^^입니다", LecNames);
    }

}