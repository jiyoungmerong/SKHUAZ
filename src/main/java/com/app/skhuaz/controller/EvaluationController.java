package com.app.skhuaz.controller;

import com.app.skhuaz.request.EvaluationSaveRequest;
import com.app.skhuaz.response.EvaluationSaveResponse;
import com.app.skhuaz.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/evaluation")
public class EvaluationController {
    private final EvaluationService evaluationService;

    @PostMapping("/save") // 강의평 저장
    public ResponseEntity<EvaluationSaveResponse> join(@RequestBody @Valid final EvaluationSaveRequest request, Principal principal) {
        EvaluationSaveResponse response = evaluationService.createEvaluation(request, principal);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}