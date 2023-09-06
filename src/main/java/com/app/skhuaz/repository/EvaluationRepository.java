package com.app.skhuaz.repository;

import com.app.skhuaz.domain.Evaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    Page<Evaluation> findAll(Pageable pageable);

    List<Evaluation> findAllByEmail(String email);

    Evaluation findByEvaluationId(Long evaluationId);
}