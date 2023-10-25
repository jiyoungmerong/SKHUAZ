package com.app.skhuaz.repository;

import com.app.skhuaz.domain.SoftwareSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SoftwareSubjectRepository extends JpaRepository<SoftwareSubject, Long> {
    List<SoftwareSubject> findBySemesterLessThanEqualAndCheckYnIsFalseOrderBySemesterDesc(String semester);

    SoftwareSubject findBySubjectNameAndSemester(String subjectName, String semester);

    @Query("SELECT s FROM SoftwareSubject s WHERE s.checkYn = true")
    List<SoftwareSubject> findByCheckYnTrue();




}
