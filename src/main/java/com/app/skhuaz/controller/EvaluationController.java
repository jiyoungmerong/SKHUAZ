package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.Evaluation;
import com.app.skhuaz.request.EvaluationSaveRequest;
import com.app.skhuaz.response.EvaluationSaveResponse;
import com.app.skhuaz.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EvaluationController {
    private final EvaluationService evaluationService;

    @PostMapping("/evaluation/create") // 강의평 저장
    public RspsTemplate<EvaluationSaveResponse> join(@RequestBody @Valid final EvaluationSaveRequest request, Principal principal) {
        return evaluationService.createEvaluation(request, principal.getName());
    }

    @GetMapping("/evaluation/AllEvaluation") // 모든 강의평 불러오기 (페이지처리 후순위)
    public RspsTemplate<List<Evaluation>> getAllEvaluation() {
        return evaluationService.getPosts();
    }

    // 해당 ID의 강의평 상세보기
    @GetMapping("/evaluation/content/{evaluationId}")
    public RspsTemplate<Evaluation> getEvaluationById(@PathVariable Long evaluationId){
        return evaluationService.getEvaluationById(evaluationId);
    }

    // 강의평 수정
    @PutMapping("/evaluation/edit/{evaluationId}")
    public RspsTemplate<EvaluationSaveResponse> updateEvaluation(
            @PathVariable Long evaluationId,
            @RequestBody @Valid  EvaluationSaveRequest evaluationRequest, Principal principal) {

        return evaluationService.updateEvaluation(evaluationId, evaluationRequest, principal.getName());
    }

    // 강의평 삭제
    @DeleteMapping("/evaluation/delete/{evaluationId}")
    public RspsTemplate<Void> deleteEvaluation(@PathVariable Long evaluationId, Principal principal) {
        evaluationService.deleteEvaluation(evaluationId, principal.getName());
        return new RspsTemplate<>(HttpStatus.OK, evaluationId + "번 강의평이 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/my-evaluations") // 내가 작성한 강의평 불러오기
    public RspsTemplate<List<Evaluation>> getMyEvaluations(Principal principal) {
        return evaluationService.getEvaluationsByEmail(principal.getName());
    }

}