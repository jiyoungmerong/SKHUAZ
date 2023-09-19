package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.Evaluation;
import com.app.skhuaz.repository.EvaluationRepository;
import com.app.skhuaz.request.EvaluationSaveRequest;
import com.app.skhuaz.request.JoinRequest;
import com.app.skhuaz.response.EvaluationSaveResponse;
import com.app.skhuaz.response.JoinResponse;
import com.app.skhuaz.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/evaluation")
public class EvaluationController {
    private final EvaluationService evaluationService;

    private final EvaluationRepository evaluationRepository;

    @PostMapping("/save") // 강의평 저장
    public RspsTemplate<EvaluationSaveResponse> join(@RequestBody @Valid final EvaluationSaveRequest request, Principal principal) {
        return evaluationService.createEvaluation(request, principal.getName());
    }

    @GetMapping("/AllEvaluation") // 모든 강의평 불러오기 (페이지처리 후순위)
    public RspsTemplate<List<Evaluation>> getAllEvaluation() {
        return evaluationService.getPosts();
    }

    // 해당 ID의 강의평 상세보기
    @GetMapping("/content/{evaluationId}")
    public RspsTemplate<Evaluation> getEvaluationById(@PathVariable Long evaluationId){
        return evaluationService.getEvaluationById(evaluationId);
    }

    // 강의평 수정
    @PutMapping("/edit/{evaluationId}")
    public RspsTemplate<EvaluationSaveResponse> updateEvaluation(
            @PathVariable Long evaluationId,
            @RequestBody @Valid  EvaluationSaveRequest evaluationRequest, Principal principal) {

        return evaluationService.updateEvaluation(evaluationId, evaluationRequest, principal.getName());

    }

    // 강의평 삭제
    @DeleteMapping("/delete/{evaluationId}")
    public RspsTemplate<Void> deleteEvaluation(@PathVariable Long evaluationId, Principal principal) {
        evaluationService.deleteEvaluation(evaluationId, principal.getName());
        return new RspsTemplate<>(HttpStatus.OK, evaluationId + "번 강의평이 성공적으로 삭제되었습니다.");
    }
    // 내가 작성한 강의평 불러오기 => 모바일쪽에서 필터처리 기능 추가한다고 함
//    @GetMapping("/my-evaluations")
//    public ResponseEntity<List<Evaluation>> getMyEvaluations(Principal principal) {
//
//        List<Evaluation> myEvaluations = evaluationRepository.findAllByEmail(principal.getName());
//        // 해당 강의평 존재하지 않을 때 처리
//
//        return ResponseEntity.status(HttpStatus.OK).body(myEvaluations);
//    }


    // 전공별로 불러오기 => 모바일쪽에서 필터처리 기능 추가한다고 함

    // 강의 이름으로 검색 => 모바일쪽에서 필터처리 기능 추가한다고 함
}