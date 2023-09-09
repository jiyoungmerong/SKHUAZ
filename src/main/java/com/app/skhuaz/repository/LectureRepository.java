package com.app.skhuaz.repository;

import com.app.skhuaz.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Optional<Lecture> findByDeptNameAndLecNameAndProfNameAndSemester(
            String deptName, String lecName, String profName, int semester);
}
