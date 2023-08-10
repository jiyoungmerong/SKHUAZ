package com.app.skhuaz.service;

import com.app.skhuaz.domain.User;
성import com.app.skhuaz.jwt.dto.TokenDto;
import com.app.skhuaz.jwt.service.TokenManager;
import com.app.skhuaz.repository.UserRepository;
import com.app.skhuaz.request.JoinRequest;
import com.app.skhuaz.response.JoinResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenManager tokenManager;

    @Transactional // 회원가입 로직
    public JoinResponse create(JoinRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 암호화
                .nickname(request.getNickname())
                .semester(request.getSemester())
                .graduate(request.isGraduate())
                .major1(request.getMajor1())
                .major2(request.getMajor2())
                .department(request.isDepartment())
                .major_minor(request.isMajor_minor())
                .double_major(request.isDouble_major())
                .build();

        userRepository.save(user);

        return JoinResponse.of(request.getEmail(), request.getNickname(), request.getSemester(),
                request.isGraduate(), request.getMajor1(), request.getMajor2(),
                request.isDepartment(), request.isMajor_minor(), request.isDouble_major());
    }

    public TokenDto login(String email, String password){ // 로그인 로직
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        TokenDto tokenDto = tokenManager.createTokenDto(String.valueOf(authenticationToken));

        return tokenDto;
    }

    public boolean checkDuplicateEmail(String email) { // 이메일 중복 확인
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean checkDuplicateNickname(String nickname) { // 닉네임 중복 확인
        return userRepository.findByNickname(nickname).isPresent();
    }
}