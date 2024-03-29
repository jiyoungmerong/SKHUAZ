package com.app.skhuaz.repository;

import com.app.skhuaz.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    @Query("SELECT DISTINCT l FROM Lecture l WHERE l.lecName = :lecName AND l.profName = :profName AND l.semester = :semester")
    Optional<Lecture> findByLecNameAndProfNameAndSemester(
            @Param("lecName") String lecName,
            @Param("profName") String profName,
            @Param("semester") String semester
    );

    @Query("SELECT DISTINCT l.semester FROM Lecture l")
    List<String> findAllSemesters();

    @Query("SELECT DISTINCT l.lecName FROM Lecture l WHERE l.semester = :semester")
    List<String> findLecturesBySemester(@Param("semester") String semester);

    @Query("SELECT DISTINCT l.profName FROM Lecture l WHERE l.semester = :semester AND l.lecName = :lecName")
    List<String> findProfessorNamesBySemesterAndLecName(
            @Param("semester") String semester,
            @Param("lecName") String lectureName
    );

}
