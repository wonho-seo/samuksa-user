package com.samuksa.user.service.user.email;

import com.samuksa.user.dto.email.auth.EmailAuth;
import com.samuksa.user.entity.errorHandler.email.EmailErrorCode;
import com.samuksa.user.entity.errorHandler.email.EmailException;
import com.samuksa.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:email.properties")
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;
    private final UserMapper userMapper;
    private String emailPassword;

    @Value("${adminMail.id}")
    private String adminMailId;

    private MimeMessage createMessage(String userEmail) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, userEmail);
        message.setSubject("samuksa 회원가입 이메일 인증");

        this.emailPassword = createKey();
        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1> 안녕하세요 samuksa입니다. </h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다!<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += emailPassword + "</strong><div><br/> ";
        msgg += "</div>";

        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress(adminMailId, "samuksa"));

        return message;
    }

    public String createKey(){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int index;
        for (int i = 0; i < 8; i++){
            index = random.nextInt(3);
            switch (index) {
                case 0:
                    sb.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    sb.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    sb.append((char) (random.nextInt(10) + 48));
                    break;
            }
        }
        return sb.toString();
    }

    @Override
    public String sendSimpleMessage(String userEmail)throws Exception {
        MimeMessage message = createMessage(userEmail);
        try{
//            javaMailSender.send(message);
            if (userMapper.getEmailKey(userEmail) == null)
                userMapper.saveEmailKey(userEmail, emailPassword);
            else
                userMapper.changeEmailKey(userEmail, emailPassword);
        }catch(MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return emailPassword;
    }

    public boolean getEmailAuth(String email, String key){
        EmailAuth emailAuth = userMapper.getEmailKey(email);
        if (emailAuth.getAuthKey().equals(key)) {
            userMapper.successAuthEmail(email);
            return true;
        }
        else
            return false;
    }
}
