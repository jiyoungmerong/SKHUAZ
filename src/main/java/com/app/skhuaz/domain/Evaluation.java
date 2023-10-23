package com.app.skhuaz.domain;

import com.app.skhuaz.request.EvaluationSaveRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name="Evaluation")
@AllArgsConstructor(access = PROTECTED)
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluationId;

    private int teamPlay; // 팀플정도

    private int task; // 과제정도

    private int practice; // 실습정도

    private int presentation; // 발표 정도

    private String title; // 제목

    private String review; // 총평

    private LocalDateTime createdAt; // 생성 날짜

    private String nickname;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lectureId")
    private Lecture lecture;

    @Builder
    public Evaluation(int teamPlay, int task, int practice, int presentation, String title,
                      String review, LocalDateTime createdAt, String nickname, Lecture lecture){
        this.teamPlay = teamPlay;
        this.task = task;
        this.practice = practice;
        this.presentation = presentation;
        this.title = title;
        this.review = review;
        this.createdAt = createdAt;
        this.nickname = nickname;
        this.lecture = lecture;
    }

    public void update(EvaluationSaveRequest request){
        this.teamPlay = request.getTeamPlay();
        this.task = request.getTask();
        this.practice = request.getPractice();
        this.presentation = request.getPresentation();
        this.title = request.getTitle();
        this.review = request.getReview();
    }

    public void updateLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
