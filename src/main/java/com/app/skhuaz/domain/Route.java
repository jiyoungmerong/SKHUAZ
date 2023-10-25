package com.app.skhuaz.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @OneToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<PreLecture> preLectures;

    public void setPreLectures(List<PreLecture> preLectures) {
        this.preLectures = preLectures;
    }

    @Builder
    public Route(String title, String recommendation, LocalDateTime createAt, String email, List<PreLecture> preLectures){
        this.title = title;
        this.recommendation = recommendation;
        this.createAt = createAt;
        this.email = email;
         this.preLectures = preLectures;
    }
}