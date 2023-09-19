package com.app.skhuaz.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import static lombok.AccessLevel.PROTECTED;

/*
강의평 작성 강의
 */
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name="Lecture")
@AllArgsConstructor(access = PROTECTED)
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    private String deptName; // 학과 이름 (ex, 소프 컴공 ... )

    private String lecName; // 강의 이름

    private String profName; // 교수님

    private String semester; // 개설 학기

    @Builder Lecture(String deptName, String lecName, String profName, String semester){
        this.deptName = deptName;
        this.lecName = lecName;
        this.profName = profName;
        this.semester = semester;
    }
}