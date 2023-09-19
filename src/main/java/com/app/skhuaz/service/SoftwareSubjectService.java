package com.app.skhuaz.service;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.SoftwareSubject;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.repository.SoftwareSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SoftwareSubjectService {

    private final SoftwareSubjectRepository softwareSubjectRepository;

    public RspsTemplate<List<SoftwareSubject>> getSubjectsBySemester(String semester) {
        List<SoftwareSubject> subjects = softwareSubjectRepository.findBySemesterLessThanEqualAndCheckYnIsFalseOrderBySemesterDesc(semester);
        if(subjects.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_EXISTS_LECNAME);

        }
        return new RspsTemplate<>(HttpStatus.OK, semester + "학기의 강의 목록 조회 성공!^^입니다", subjects);
    }

}

