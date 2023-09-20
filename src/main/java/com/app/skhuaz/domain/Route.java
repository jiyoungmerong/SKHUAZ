package com.app.skhuaz.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name="Route")
@AllArgsConstructor(access = PROTECTED)
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int RouteId;

    private String title; // 제목

    private String recommendation; // 내용

    private LocalDateTime createAt;

    private String email;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore // preLectures 필드를 JSON 직렬화에서 제외
    private List<PreLecture> preLectures;

    public void setPreLectures(List<PreLecture> preLectures) {
        this.preLectures = preLectures;
    }

    @Builder
    public Route(String title, String recommendation, LocalDateTime createAt, String email){
        this.title = title;
        this.recommendation = recommendation;
        this.createAt = createAt;
        this.email = email;
        // this.preLectures = preLectures;
    }
}