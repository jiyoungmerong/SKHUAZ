package com.app.skhuaz.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    private boolean clickYn; // 클릭 가능 여부

    private String semester;

    public void updateCheckYn(){
        this.checkYn = true;
    }

    @Builder
    public SoftwareSubject(String subjectName, String semester, boolean checkYn, boolean clickYn){
        this.subjectName = subjectName;
        this.semester = semester;
        this.checkYn = false;
        this.clickYn = false;
    }

}