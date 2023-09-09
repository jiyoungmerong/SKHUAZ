package com.app.skhuaz.domain;

import com.app.skhuaz.request.UpdateUserInformationRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private Long userId;

    @Column(unique = true, nullable = false)
    @NotNull(message = "이메일은 필수 입력값입니다.") // 수정된 부분
    private String email;

    @NonNull
    private String password; // 비밀번호

    @Column(unique = true, nullable = false, length = 10)
    private String nickname; // 닉네임

    @NonNull
    private int semester; // 학기

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

    public void login(boolean isLogin) { // 로그인
        this.isLogin = true;
    }

    public void logout(boolean isLogin) { // 로그아웃
        this.isLogin = false;
    }

    @Builder
    public User(String email, String password, String nickname, int semester,
                boolean graduate, String major1, String major2, boolean department,
                boolean major_minor, boolean double_major){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.semester = semester;
        this.graduate = graduate;
        this.major1 = major1;
        this.major2 = major2;
        this.department = department;
        this.major_minor = major_minor;
        this.double_major = double_major;
    }

    public void updateUser(UpdateUserInformationRequest request){
        this.nickname = request.getNickname();
        this.semester = request.getSemester();
        this.graduate = request.isGraduate();
        this.major1 = request.getMajor1();
        this.major2 = request.getMajor2();
        this.department = request.isDepartment();
        this.major_minor = request.isMajor_minor();
        this.double_major = request.isDouble_major();
    }

    public String updatePassword(String password){ // 비밀번호 업데이트
        this.password = password;
        return password;
    }

    // fixme : 추후에 권한 추가 예정
}