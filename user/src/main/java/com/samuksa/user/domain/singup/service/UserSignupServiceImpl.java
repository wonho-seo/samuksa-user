package com.samuksa.user.domain.singup.service;

import com.samuksa.user.db.table.samuksa_user_db.entity.AuthEmail;
import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.repository.AuthEmailRepository;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import com.samuksa.user.domain.singup.dto.request.SignupRequest;
import com.samuksa.user.domain.singup.email.service.email.EmailServiceImpl;
import com.samuksa.user.errorexception.entity.errorHandler.email.EmailErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.email.EmailException;
import com.samuksa.user.errorexception.entity.errorHandler.signup.SignupErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.signup.SignupException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignupServiceImpl {

    private final AuthEmailRepository authEmailRepository;
    private final CustUserRepository custUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailServiceImpl emailService;
    @Value("${image.upload.path}")
    private String uploadPath;
    @Value("${image.show.path}")
    private String showPath;
    public void existence(SignupRequest signupRequest){
        if (signupRequest.getUserId() == null && signupRequest.getEmail() == null
                && signupRequest.getNickName() == null)
            throw new SignupException("existence null", SignupErrorCode.EXISTENCE_NO_VARIABLE);
        if (signupRequest.getUserId() != null && custUserRepository.existsByUserId(signupRequest.getUserId()))
            throw new SignupException("existence id", SignupErrorCode.EXISTENCE_ID);
        if (signupRequest.getEmail() != null && custUserRepository.existsByEmail(signupRequest.getEmail()))
            throw new SignupException("existence email", SignupErrorCode.EXISTENCE_EMAIL);
        if (signupRequest.getNickName() != null && custUserRepository.existsByNickName(signupRequest.getNickName()))
            throw new SignupException("existence nickname", SignupErrorCode.EXISTENCE_NICKNAME);
    }

    public void signup(SignupRequest signupRequest){
        existence(signupRequest);
        AuthEmail authEmail = authEmailRepository.findByauthEmail(signupRequest.getEmail()).orElseThrow(() -> new EmailException("exist", EmailErrorCode.Wrong_Approach));
        if (authEmail.getAuth() != 1)
            throw new EmailException("exist", EmailErrorCode.Wrong_Approach);
        CustUser custUser = CustUser.builder()
                .userId(signupRequest.getUserId())
                .email(signupRequest.getEmail())
                .nickName(signupRequest.getNickName())
                .authentication("ROLE_USER")
                .userPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .profileImagePath(showPath + "defaultprofileimage.png")
                .build();
        custUserRepository.save(custUser);
    }

    public ResponseEntity<String> authEmail(SignupRequest signupRequest) throws Exception{
        if (signupRequest.getAuthKey() == null)
            return ResponseEntity.status(200).body(emailService.sendSimpleMessage(signupRequest.getEmail()));
        if (!emailService.getEmailAuth(signupRequest.getEmail(), signupRequest.getAuthKey()))
            return ResponseEntity.status(400).body("not match");
        return ResponseEntity.status(200).body("success");
    }


}
