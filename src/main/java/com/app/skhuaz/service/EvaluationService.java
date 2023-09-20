package com.app.skhuaz.service;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.Evaluation;
import com.app.skhuaz.domain.Lecture;
import com.app.skhuaz.domain.User;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.repository.EvaluationRepository;
import com.app.skhuaz.repository.UserRepository;
import com.app.skhuaz.request.EvaluationSaveRequest;
import com.app.skhuaz.response.EvaluationSaveResponse;
import com.app.skhuaz.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;

    private final LectureRepository lectureRepository;

    private final UserRepository userRepository;

    @Transactional // 강의평 저장
    public RspsTemplate<EvaluationSaveResponse> createEvaluation(EvaluationSaveRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_JOIN));

        Lecture lecture = lectureRepository.findByDeptNameAndLecNameAndProfNameAndSemester(
                        request.getDeptName(), request.getLecName(), request.getProfName(), request.getSemester())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTS_LECTURE));

        try {
            Evaluation evaluation = Evaluation.builder()
                    .lecture(lecture)
                    .email(email)
                    .teamPlay(request.getTeamPlay())
                    .task(request.getTask())
                    .practice(request.getPractice())
                    .presentation(request.getPresentation())
                    .title(request.getTitle())
                    .review(request.getReview())
                    .createdAt(LocalDateTime.now())
                    .build();

            evaluationRepository.save(evaluation);

            return new RspsTemplate<>(HttpStatus.OK, "강의평이 성공적으로 저장되었습니다.", EvaluationSaveResponse.of(lecture.getDeptName(), lecture.getLecName(), lecture.getProfName(), lecture.getSemester(),
                    request.getTeamPlay(), request.getTask(), request.getPractice(), request.getPresentation(), request.getTitle(),
                    request.getReview()));
        }
        catch (Exception e){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional // 강의평 업데이트
    public RspsTemplate<EvaluationSaveResponse> updateEvaluation(Long evaluationId, EvaluationSaveRequest request, String email) {
        // 평가 엔티티 조회
        Evaluation evaluation = evaluationRepository.findByEvaluationId(evaluationId);

        if (evaluation == null) {
            throw new BusinessException(ErrorCode.NOT_EXISTS_EVALUATION);
        }

        // 현재 사용자가 평가 작성자인지 확인
        if (!Objects.equals(email, evaluation.getEmail())) {
            throw new BusinessException(ErrorCode.NOT_EXISTS_AUTHORITY);
        }

        // 강의 엔티티를 찾기 위해 강의 정보로 검색
        Lecture lecture = lectureRepository.findByDeptNameAndLecNameAndProfNameAndSemester(
                        request.getDeptName(), request.getLecName(), request.getProfName(), request.getSemester())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTS_LECTURE));

        // Evaluation 엔티티 수정
        evaluation.update(request);
        // lecture 속성 수정
        evaluation.updateLecture(lecture);

        evaluationRepository.save(evaluation);

        return new RspsTemplate<>(HttpStatus.OK, "id가 " + evaluationId + "인 강의평을 성공적으로 수정하였습니다.", EvaluationSaveResponse.of(lecture.getDeptName(), lecture.getLecName(), lecture.getProfName(), lecture.getSemester(),
                request.getTeamPlay(), request.getTask(), request.getPractice(), request.getPresentation(), request.getTitle(),
                request.getReview()));
    }

    @Transactional
    public void deleteEvaluation(Long evaluationId, String email) { // 강의평 삭제
        Evaluation evaluation = evaluationRepository.findByEvaluationId(evaluationId);

        if (evaluation == null) { // 해당 강의평이 존재하지 않는다면
            throw new BusinessException(ErrorCode.NOT_EXISTS_EVALUATION);
        }

        if (!email.equals(evaluation.getEmail()) && !email.equals("admin@admin.com")) { // 해당 강의평을 작성한 사용자가 아니라면
            throw new BusinessException(ErrorCode.NOT_EXISTS_AUTHORITY);
        }
        try{
            evaluationRepository.delete(evaluation);
        }
        catch (Exception e) { // 서버 오류
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 모든 강의평 불러오기
    public RspsTemplate<List<Evaluation>> getPosts() {
        List<Evaluation> evaluations = evaluationRepository.findAllByOrderByEvaluationIdDesc();
        if (evaluations.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_EXISTS_EVALUATION);
        }
        return new RspsTemplate<>(HttpStatus.OK, "모든 강의평 불러오기에 성공했습니다.", evaluations);
    }



    public RspsTemplate<Evaluation> getEvaluationById(@PathVariable Long evaluationId){
        Evaluation evaluation = evaluationRepository.findByEvaluationId(evaluationId);
        if(evaluation==null){
            throw new BusinessException(ErrorCode.NOT_EXISTS_EVALUATION);
        }
        return new RspsTemplate<>(HttpStatus.OK, "id가 " + evaluationId + "인 강의평을 성공적으로 불러왔습니다.", evaluation);
    }


//    public RspsTemplate<List<Evaluation>> getMyEvaluations(String email) {
//
//        List<Evaluation> myEvaluations = evaluationRepository.findAllByEmail(email);
//        if(myEvaluations.isEmpty()){
//            throw new BusinessException(ErrorCode.NOT_EXISTS_EVALUATION);
//        }
//
//        return new RspsTemplate<>(HttpStatus.OK, myEvaluations, "강의평 불러오기에 성공했습니다.");
//    }
}