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

    private String review; // 총평

    @ManyToOne
    @JoinColumn(name = "lectureId")
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Builder
    public Evaluation(int teamPlay, int task, int practice, int presentation,
                      String review, Lecture lecture, User user){
        this.teamPlay = teamPlay;
        this.task = task;
        this.practice = practice;
        this.presentation = presentation;
        this.review = review;
        this.lecture = lecture;
        this.user = user;
    }
}
