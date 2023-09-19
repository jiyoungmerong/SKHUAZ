package com.app.skhuaz.repository;

import com.app.skhuaz.domain.Evaluation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    List<Evaluation> findAllByOrderByEvaluationIdDesc();
    Evaluation findByEvaluationId(Long evaluationId);
}