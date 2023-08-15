package com.app.skhuaz.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailsender; // JavaMailSender autowired

    private final EmailVerificationService emailVerificationService;

    private String authNum; // 인증번호

    // 메일 양식
    public MimeMessage createJoinMessage(String email) throws MessagingException {
        authNum = emailVerificationService.generateCode(email);
        String setFrom = "gjwldud0719@naver.com";
        String title = "회원가입 인증 번호";

        MimeMessage message = emailsender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); // 보내는 대상
        message.setSubject(title);
        message.setFrom(setFrom);
        String n = ""; // 보낼 메일 양식 html
        n += "<div style='display: flex; width: 100%; min-height: 100%; text-align: center; justify-content: center; background-color: #f0f0f0;'>";
        n += "<div style='display: flex; width: 80%; padding-top: 5%; background-color: white; text-align: left; justify-content: center;'>";
        n += "<div style='width: 90%; background-color: white; text-align: left;'>";
        n += "<h1 style='color: black; text-align: center;'>SKHUAZ 회원가입 안내</h1>";
        n += "<br />";
        n += "<p style='text-align: center;'><strong style='color: #444444;'>\"회원가입\"</strong>을 위해 이메일 인증을 진행합니다.</p>";
        n += "<p style='text-align: center;'>아래에 발급된 이메일 인증번호를 복사하거나 직접 입력하여 인증을 완료해 주세요.</p>";
        n += "<br />";
        n += "<hr />";
        n += "<p style='text-align: center;'>인증번호: <strong style='letter-spacing: 5px; color: blue;'>" + authNum + "</strong></p>";
        n += "<br />";
        n += "<br />";
        n += "<br />";
        n += "<hr />";
        n += "</div>";
        n += "</div>";
        n += "</div>";
        n += "</div>";

        message.setText(n,"utf-8", "html");
        return message;
    }

    public void sendJoinMessage(String email) throws Exception{ // 회원가입 메일 발송
        MimeMessage message = createJoinMessage(email);
        try{
            emailsender.send((message));
        } catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}