package com.app.skhuaz.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name="PreLecture")
@AllArgsConstructor(access = PROTECTED)
public class PreLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preLectureId;

    private String semester;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> lecNames;

    private String email;

    @Builder
    public PreLecture(Long preLectureId, String semester, String email, List<String> lecNames){
        this.preLectureId = preLectureId;
        this.semester = semester;
        this.email = email;
        this.lecNames = lecNames;
    }
}
