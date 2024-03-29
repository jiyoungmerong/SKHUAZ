package com.app.skhuaz.domain;

import com.app.skhuaz.request.RouteEditRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.List;

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

    private String nickname;

    @OneToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<PreLecture> preLectures;

    @Builder
    public Route(String title, String recommendation, LocalDateTime createAt, String email, String nickname, List<PreLecture> preLectures){
        this.title = title;
        this.recommendation = recommendation;
        this.createAt = createAt;
        this.email = email;
        this.nickname = nickname;
        this.preLectures = preLectures;
    }

    public void update(RouteEditRequest request){
        this.title = request.getTitle();
        this.recommendation= request.getRecommendation();

    }
}