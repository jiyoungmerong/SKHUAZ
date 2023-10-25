package com.app.skhuaz.repository;

import com.app.skhuaz.domain.Prerequisites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrerequisitesRepository extends JpaRepository<Prerequisites, Long> {

    List<Prerequisites> findBySubjectId(Long subjectId);

}