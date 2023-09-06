package com.app.skhuaz.controller;

import com.app.skhuaz.domain.Evaluation;
import com.app.skhuaz.repository.EvaluationRepository;
import com.app.skhuaz.request.EvaluationSaveRequest;
import com.app.skhuaz.response.EvaluationSaveResponse;
import com.app.skhuaz.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<EvaluationSaveResponse> join(@RequestBody @Valid final EvaluationSaveRequest request, Principal principal) {
        EvaluationSaveResponse response = evaluationService.createEvaluation(request, principal);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/AllEvaluation") // 모든 강의평 불러오기 (페이지처리 후순위)
    public ResponseEntity<Page<Evaluation>> getPosts(@PageableDefault(size = 10, sort = "evaluationId", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Evaluation> evaluationPage = evaluationRepository.findAll(pageable);
        return ResponseEntity.ok(evaluationPage);
    }

    // 내가 작성한 강의평 불러오기
    @GetMapping("/my-evaluations")
    public ResponseEntity<List<Evaluation>> getMyEvaluations(Principal principal) {

        List<Evaluation> myEvaluations = evaluationRepository.findAllByEmail(principal.getName());

        return ResponseEntity.status(HttpStatus.OK).body(myEvaluations);
    }

    // 해당 ID의 강의평 상세보기 // 해당 강의평 없을 때 오류 처리 해야함~
    @GetMapping("/content/{evaluationId}")
    public ResponseEntity<Evaluation> getEvaluationById(@PathVariable Long evaluationId){
        Evaluation evaluation = evaluationRepository.findByEvaluationId(evaluationId);
        return ResponseEntity.status(HttpStatus.OK).body(evaluation);
    }

    // 강의평 수정
    @PutMapping("/edit/{evaluationId}")
    public ResponseEntity<EvaluationSaveResponse> updateEvaluation(
            @PathVariable Long evaluationId,
            @RequestBody @Valid  EvaluationSaveRequest evaluationRequest, Principal principal) {

        EvaluationSaveResponse response = evaluationService.updateEvaluation(evaluationId, evaluationRequest, principal);

        return ResponseEntity.ok(response);
    }

    // 강의평 삭제
    @DeleteMapping("/delete/{evaluationId}")
    public ResponseEntity<String> deleteEvaluation(@PathVariable Long evaluationId, Principal principal) {
        evaluationService.deleteEvaluation(evaluationId, principal);
        return ResponseEntity.ok(evaluationId + "번 강의평이 성공적으로 삭제되었습니다.");
    }


    // 전공별로 불러오기

    // 강의 이름으로 검색
}