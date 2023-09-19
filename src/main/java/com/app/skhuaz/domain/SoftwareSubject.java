package com.app.skhuaz.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name="SoftwareSubject")
@AllArgsConstructor(access = PROTECTED)
public class SoftwareSubject { // 소프의 모든 강의 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId; // 소프 과목 id

    @Column(unique = true, nullable = false)
    private String subjectName; // 소프 과목 이름

    private boolean checkYn; // 체크 확인

    private String semester;

}