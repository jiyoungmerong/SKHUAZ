package com.app.skhuaz.service;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.User;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.jwt.dto.TokenDto;
import com.app.skhuaz.jwt.service.TokenManager;
import com.app.skhuaz.repository.UserRepository;
import com.app.skhuaz.request.*;
import com.app.skhuaz.response.JoinResponse;
import com.app.skhuaz.response.UserMainInformationResponse;
import com.app.skhuaz.util.EntityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    private final EmailVerificationService emailVerificationService;

    private final TokenManager tokenManager;

    private final PasswordEncoder passwordEncoder;

    @Transactional // 회원가입
    public RspsTemplate<JoinResponse> create(@Valid JoinRequest request) {
        boolean isEmailVerified = emailVerificationService.isEmailVerified(request.getEmail());
        if(!isEmailVerified){ // 이메일 인증 하지 않았을 때
            throw new BusinessException(ErrorCode.NOT_EMAIL_VERIFY);
        } else if (userRepository.findByEmail(request.getEmail()).isPresent()) { // 이미 존재하는 이메일일 때
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_REGISTERED);
        }
        else if (userRepository.findByNickname(request.getNickname()).isPresent()) // 이미 존재하는 닉네임일 때
            throw new BusinessException(ErrorCode.NICKNAME_ALREADY_CHECK);
        else if(request.getNickname().length()>10){ // 닉네임 길이가 10이 넘을 때
            throw new BusinessException(ErrorCode.NICKNAME_LENGTH_MAX);
        }

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

        return new RspsTemplate<>(HttpStatus.OK, "회원가입이 성공적으로 완료되었습니다.", JoinResponse.of(request.getEmail(), request.getNickname(), request.getSemester(),
                request.isGraduate(), request.getMajor1(), request.getMajor2(),
                request.isDepartment(), request.isMajor_minor(), request.isDouble_major()));
    }

    public TokenDto login(String email, String password) { // 로그인 로직
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) { // 유저가 존재하지 않을 때
            throw new BusinessException(ErrorCode.USER_NOT_JOIN);
        } else if (!passwordEncoder.matches(password, user.get().getPassword())) { // 비밀번호가 옳지 않을 때
            throw new BusinessException(ErrorCode.USER_CERTIFICATION_FAILED);
        }

        return tokenManager.createTokenDto(email);
    }

    public RspsTemplate<String> checkDuplicateNickname(String nickname) { // 닉네임 중복 확인
        boolean checkNickname = userRepository.findByNickname(nickname).isPresent();
        if (checkNickname) { // 중복일 때
            throw new BusinessException(ErrorCode.NICKNAME_ALREADY_REGISTERED);
        } else {
            return new RspsTemplate<>(HttpStatus.OK, "사용 가능한 닉네임입니다.", nickname);
        }
    }

    @Transactional
    public RspsTemplate<UpdateUserInformationRequest> updateUserInformation(UpdateUserInformationRequest request, String email) { // 유저 정보 업데이트
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_JOIN));

        boolean checkNickname = userRepository.findByNickname(request.getNickname()).isPresent();
        if (checkNickname) { // 닉네임 중복일 때
            throw new BusinessException(ErrorCode.NICKNAME_ALREADY_REGISTERED);
        }

        user.updateUser(request);
        return new RspsTemplate<>(HttpStatus.OK, "변경 성공", request);
    }

    public RspsTemplate<Void> checkUser(LoginRequest request, String email) { // 유저 확인
        Optional<User> creator = userRepository.findByEmail(email); // 이메일에 해당하는 유저 가져오기
        if (creator.isEmpty() || !email.equals(request.getEmail())) {
            throw new BusinessException(ErrorCode.USER_NOT_JOIN);
        } else if (!passwordEncoder.matches(request.getPassword(), creator.get().getPassword())) {
            throw new BusinessException(ErrorCode.USER_CERTIFICATION_FAILED);
        }
        return new RspsTemplate<>(HttpStatus.OK, "인증 성공");
    }

    @Transactional
    public void logout(String email) { // 로그아웃
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = EntityUtil.mustNotNull(optionalUser, ErrorCode.USER_NOT_JOIN);
        user.logout();
    }

    @Transactional
    public RspsTemplate<String> deleteUser(Principal principal){ // 회원탈퇴
        Optional<User> user = userRepository.findByEmail(principal.getName());
        try{
            userRepository.delete(user.get());
            return new RspsTemplate<>(HttpStatus.OK, principal.getName(), "탈퇴에 성공하였습니다.");
        } catch (Exception e){
            throw new BusinessException(ErrorCode.USER_CERTIFICATION_FAILED);
        }
    }


    public RspsTemplate<UserMainInformationResponse> getUserInfo(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        String nickname = userOptional.get().getNickname();
        String major1 = userOptional.get().getMajor1();
        String major2 = userOptional.get().getMajor2();
        String semester = userOptional.get().getSemester();
        boolean graduate = userOptional.get().isGraduate();
        boolean department = userOptional.get().isDepartment();
        boolean major_minor = userOptional.get().isMajor_minor();
        boolean double_major = userOptional.get().isDouble_major();


        return new RspsTemplate<>(HttpStatus.OK, "유저 정보 조회 성공!!",
                UserMainInformationResponse.of(nickname, major1, major2, semester, graduate, department, major_minor, double_major));

    }
}