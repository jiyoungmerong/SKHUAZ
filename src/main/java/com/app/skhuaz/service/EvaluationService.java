package com.app.skhuaz.service;

import com.app.skhuaz.domain.Evaluation;
import com.app.skhuaz.domain.Lecture;
import com.app.skhuaz.domain.User;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.repository.EvaluationRepository;
import com.app.skhuaz.repository.UserRepository;
import com.app.skhuaz.request.EvaluationSaveRequest;
import com.app.skhuaz.response.EvaluationSaveResponse;
import com.app.skhuaz.response.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.util.Objects;

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

        try {
            Evaluation evaluation = Evaluation.builder()
                    .lecture(lecture)
                    .email(principal.getName())
                    .teamPlay(request.getTeamPlay())
                    .task(request.getTask())
                    .practice(request.getPractice())
                    .presentation(request.getPresentation())
                    .title(request.getTitle())
                    .review(request.getReview())
                    .build();

            evaluationRepository.save(evaluation);

            return EvaluationSaveResponse.of(lecture.getDeptName(), lecture.getLecName(), lecture.getProfName(), lecture.getSemester(),
                    request.getTeamPlay(), request.getTask(), request.getPractice(), request.getPresentation(), request.getTitle(),
                    request.getReview());
        }
        catch (Exception e){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional // 강의평 업데이트
    public EvaluationSaveResponse updateEvaluation(Long evaluationId, EvaluationSaveRequest request, Principal principal) {
        // 평가 엔티티 조회
        Evaluation evaluation = evaluationRepository.findByEvaluationId(evaluationId);

        if (evaluation == null) {
            throw new BusinessException(ErrorCode.NOT_EXISTS_EVALUATION);
        }

        // 현재 사용자가 평가 작성자인지 확인
        if (!Objects.equals(principal.getName(), evaluation.getEmail())) {
            throw new BusinessException(ErrorCode.NOT_EXISTS_AUTHORITY);
        }

        // 강의 엔티티를 찾기 위해 강의 정보로 검색
        Lecture lecture = lectureRepository.findByDeptNameAndLecNameAndProfNameAndSemester(
                        request.getDeptName(), request.getLecName(), request.getProfName(), request.getSemester())
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        // Evaluation 엔티티 수정
        evaluation.update(request);
        // lecture 속성 수정
        evaluation.updateLecture(lecture);

        evaluationRepository.save(evaluation);

        return EvaluationSaveResponse.of(lecture.getDeptName(), lecture.getLecName(), lecture.getProfName(), lecture.getSemester(),
                request.getTeamPlay(), request.getTask(), request.getPractice(), request.getPresentation(), request.getTitle(),
                request.getReview());
    }

    @Transactional
    public void deleteEvaluation(Long evaluationId, Principal principal) { // 강의평 삭제
        try{
            Evaluation evaluation = evaluationRepository.findByEvaluationId(evaluationId);

            if (evaluation == null) { // 해당 강의평이 존재하지 않는다면
                throw new BusinessException(ErrorCode.NOT_EXISTS_EVALUATION);
            }

            if (!principal.getName().equals(evaluation.getEmail())) { // 해당 강의평을 작성한 사용자가 아니라면
                throw new BusinessException(ErrorCode.NOT_EXISTS_AUTHORITY);
            }

            evaluationRepository.delete(evaluation);
        }
        catch (Exception e) { // 서버 오류
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 저장시 카테고리 학기 - 교수님 - 강의이름 필터 => lecture컨트롤러 생성
}