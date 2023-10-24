package com.app.skhuaz.repository;

import com.app.skhuaz.domain.Prerequisites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrerequisitesRepository extends JpaRepository<Prerequisites, Long> {
//    List<Prerequisites> findBySubject_SubjectId(Long subjectId);
//    List<Prerequisites> findByPrerequisite_SubjectId(Long subjectId);

    List<Prerequisites> findBySubjectId(Long subjectId);





}
