package com.samuksa.user.domain.singup.service;

import com.samuksa.user.db.table.samuksa_user_db.entity.AuthEmail;
import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.entity.UserImage;
import com.samuksa.user.db.table.samuksa_user_db.repository.AuthEmailRepository;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import com.samuksa.user.db.table.samuksa_user_db.repository.UserImageRepository;
import com.samuksa.user.domain.singup.dto.request.SignupRequest;
import com.samuksa.user.domain.singup.service.email.EmailServiceImpl;
import com.samuksa.user.errorexception.entity.errorHandler.email.EmailErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.email.EmailException;
import com.samuksa.user.errorexception.entity.errorHandler.signup.SignupErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.signup.SignupException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class UserSignupServiceImpl {

    private final AuthEmailRepository authEmailRepository;
    private final CustUserRepository custUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailServiceImpl emailService;
    private final UserImageRepository userImageRepository;
    @Value("${image.upload.path}")
    private String uploadPath;

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
        AuthEmail authEmail = authEmailRepository.findByauthEmail(signupRequest.getEmail());
        if (authEmail == null || authEmail.getAuth() != 1)
            throw new EmailException("exist", EmailErrorCode.Wrong_Approach);
        CustUser custUser = CustUser.builder()
                .userId(signupRequest.getUserId())
                .email(signupRequest.getEmail())
                .nickName(signupRequest.getNickName())
                .name(signupRequest.getName())
                .authentication("ROLE_USER")
                .userPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .build();
        makeFolder(custUser.getUserId());
        UserImage userImage = UserImage.builder()
                .custUser(custUser)
                .build();
        custUserRepository.save(custUser);
        userImageRepository.save(userImage);
    }

    public ResponseEntity<String> authEmail(SignupRequest signupRequest) throws Exception{
        if (signupRequest.getAuthKey() == null)
            return ResponseEntity.status(200).body(emailService.sendSimpleMessage(signupRequest.getEmail()));
        if (!emailService.getEmailAuth(signupRequest.getEmail(), signupRequest.getAuthKey()))
            return ResponseEntity.status(400).body("not match");
        return ResponseEntity.status(200).body("success");
    }

    private void makeFolder(String userId) {
        File uploadPathFolder = new File(uploadPath, userId);
        String defaultImage = uploadPath + File.separator + userId + File.separator + "defaultprofileimage.png";
        if (uploadPathFolder.exists() == false)
            uploadPathFolder.mkdirs();
        return;
    }
}
