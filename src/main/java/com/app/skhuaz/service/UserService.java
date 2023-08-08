package com.app.skhuaz.service;

import com.app.skhuaz.domain.User;
import com.app.skhuaz.jwt.dto.TokenDto;
import com.app.skhuaz.jwt.service.TokenManager;
import com.app.skhuaz.repository.UserRepository;
import com.app.skhuaz.request.JoinRequest;
import com.app.skhuaz.response.JoinResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenManager tokenManager;

    @Transactional
    public JoinResponse create(JoinRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
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

    public TokenDto login(String email, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        TokenDto tokenDto = tokenManager.createTokenDto(String.valueOf(authenticationToken));

        return tokenDto;
    }

    public boolean checkDuplicateEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean validateUserPassword(String currentPassword, String loggedInUserPassword) {
        return passwordEncoder.matches(currentPassword, loggedInUserPassword);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}