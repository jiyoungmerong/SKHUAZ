package com.app.skhuaz.service;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.User;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.jwt.dto.TokenDto;
import com.app.skhuaz.jwt.service.TokenManager;
import com.app.skhuaz.repository.UserRepository;
import com.app.skhuaz.request.JoinRequest;
import com.app.skhuaz.request.LoginRequest;
import com.app.skhuaz.request.UpdateUserInformationRequest;
import com.app.skhuaz.response.JoinResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenManager tokenManager;

    @Transactional // 회원가입
    public JoinResponse create(JoinRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_REGISTERED);
        }
        else if(userRepository.findByNickname(request.getNickname()).isPresent())
            throw new BusinessException(ErrorCode.NICKNAME_ALREADY_REGISTERED);

        try {
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
        catch (Exception e){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public TokenDto login(String email, String password){ // 로그인 로직
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) { // 유저가 존재하지 않을 때
            throw new BusinessException(ErrorCode.USER_NOT_JOIN);
        }
        if (!passwordEncoder.matches(password, user.get().getPassword())) { // 비밀번호가 옳지 않을 때
            throw new BusinessException(ErrorCode.USER_CERTIFICATION_FAILED);
        }

        TokenDto tokenDto = tokenManager.createTokenDto(email);

        return tokenDto;
    }

    public boolean checkDuplicateNickname(String nickname) { // 닉네임 중복 확인
        return userRepository.findByNickname(nickname).isPresent();
    }

    public void deleteUser(Long id){ // 탈퇴
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new BusinessException(ErrorCode.USER_NOT_JOIN);
        }
        userRepository.delete(user.get());
    }

    public void updateUserInformation(User user, UpdateUserInformationRequest request) {
        User updateUser = User.builder()
                .nickname(request.getNickname())
                .semester(request.getSemester())
                .graduate(request.isGraduate())
                .major1(request.getMajor1())
                .major2(request.getMajor2())
                .department(request.isDepartment())
                .major_minor(request.isMajor_minor())
                .double_major(request.isDouble_major())
                .build();
        userRepository.save(updateUser);
    }
}