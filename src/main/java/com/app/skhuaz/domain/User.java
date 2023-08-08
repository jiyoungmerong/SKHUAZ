package com.app.skhuaz.domain;

import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

/*
User 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name="User")
@AllArgsConstructor(access = PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email; // 이메일

    @NonNull
    private String password; // 비밀번호

    @Column(unique = true, nullable = false, length = 10)
    private String nickname; // 닉네임

    @NonNull
    private String semester; // 학기

    @NonNull
    private boolean graduate; // 졸업 여부

    @NonNull
    private String major1; // 전공 1

    @NonNull
    private String major2; // 전공 2

    @NonNull
    private boolean department; // 학부 여부

    @NonNull
    private boolean major_minor; // 주-부전공 여부

    @NonNull
    private boolean double_major; // 복수전공 여부

    private boolean isLogin; // 로그인 여부

    // fixme : 추후에 권한 추가 예정

}
