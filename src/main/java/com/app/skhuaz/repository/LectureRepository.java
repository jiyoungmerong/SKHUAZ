package com.app.skhuaz.repository;

import com.app.skhuaz.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Optional<Lecture> findByDeptNameAndLecNameAndProfNameAndSemester(
            String deptName, String lecName, String profName, String semester);

    @Query("SELECT DISTINCT l.semester FROM Lecture l")
    List<String> findAllSemesters();

//    @Query("SELECT DISTINCT l.profName FROM Lecture l WHERE l.semester = :semester")
//    List<String> findProfessorsBySemester(@Param("semester") String semester);
//
//    @Query("SELECT l.lecName FROM Lecture l WHERE l.semester = :semester AND l.profName = :professorName")
//    List<String> findLectureNameBySemesterAndProfessor(
//            @Param("semester") String semester,
//            @Param("professorName") String professorName);

    @Query("SELECT DISTINCT l.lecName FROM Lecture l WHERE l.semester = :semester")
    List<String> findLecturesBySemester(@Param("semester") String semester);

    @Query("SELECT DISTINCT l.profName FROM Lecture l WHERE l.semester = :semester AND l.lecName = :lecName")
    List<String> findProfessorNamesBySemesterAndLecName(
            @Param("semester") String semester,
            @Param("lecName") String lectureName
    );

}
