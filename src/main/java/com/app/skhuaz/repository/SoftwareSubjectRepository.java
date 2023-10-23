package com.app.skhuaz.repository;

import com.app.skhuaz.domain.SoftwareSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SoftwareSubjectRepository extends JpaRepository<SoftwareSubject, Long> {
    List<SoftwareSubject> findBySemesterLessThanEqualAndCheckYnIsFalseOrderBySemesterDesc(String semester);

    Optional<SoftwareSubject> findBySubjectName(String subjectName);

    Optional<SoftwareSubject> findBySubjectId(Long subjectId);

    Long findSubjectIdBySubjectName(String subjectName);

    SoftwareSubject findBySubjectNameAndSemester(String subjectName, String semester);



}
