package com.app.skhuaz.repository;

import com.app.skhuaz.domain.PreLecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreLectureRepository extends JpaRepository<PreLecture, Long> {
    List<PreLecture> findByEmail(String email);


}
