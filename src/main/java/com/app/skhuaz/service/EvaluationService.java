package com.app.skhuaz.service;

import com.app.skhuaz.domain.Evaluation;
import com.app.skhuaz.domain.Lecture;
import com.app.skhuaz.domain.User;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.repository.EvaluationRepository;
import com.app.skhuaz.repository.UserRepository;
import com.app.skhuaz.request.EvaluationSaveRequest;
import com.app.skhuaz.request.JoinRequest;
import com.app.skhuaz.response.EvaluationSaveResponse;
import com.app.skhuaz.response.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;

    private final LectureRepository lectureRepository;

    private final UserRepository userRepository;

    @Transactional // 강의평 저장
    public EvaluationSaveResponse createEvaluation(EvaluationSaveRequest request, Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Lecture lecture = lectureRepository.findByDeptNameAndLecNameAndProfNameAndSemester(
                        request.getDeptName(), request.getLecName(), request.getProfName(), request.getSemester())
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        // fixme 로그인 되어있지 않을 때 401 처리
//        if(user.isLogin()==false){ // 로그인 되어 있지 않을 때
//            throw new BusinessException(ErrorCode.STATUS_NOT_LOGIN);
//        }

        try{
            Evaluation evaluation = Evaluation.builder()
                    .lecture(lecture)
                    .user(user)
                    .teamPlay(request.getTeamPlay())
                    .task(request.getTask())
                    .practice(request.getPractice())
                    .presentation(request.getPresentation())
                    .review(request.getReview())
                    .build();

            Evaluation savedEvaluation = evaluationRepository.save(evaluation);

            return EvaluationSaveResponse.of(lecture.getDeptName(), lecture.getLecName(), lecture.getProfName(), lecture.getSemester(),
                    request.getTeamPlay(), request.getTask(), request.getPractice(), request.getPresentation(), request.getReview(), user.getNickname());
        }
        catch (Exception e){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}