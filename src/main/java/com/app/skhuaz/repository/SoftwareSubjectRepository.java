package com.app.skhuaz.repository;

import com.app.skhuaz.domain.SoftwareSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SoftwareSubjectRepository extends JpaRepository<SoftwareSubject, Long> {
    List<SoftwareSubject> findBySemesterLessThanEqualAndCheckYnIsFalseOrderBySemesterDesc(String semester);

}
