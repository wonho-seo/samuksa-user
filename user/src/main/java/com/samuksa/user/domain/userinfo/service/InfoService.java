package com.samuksa.user.domain.userinfo.service;

import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.entity.UserImage;
import com.samuksa.user.db.table.samuksa_user_db.entity.UserJwtToken;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import com.samuksa.user.db.table.samuksa_user_db.repository.UserImageRepository;
import com.samuksa.user.db.table.samuksa_user_db.repository.UserJwtTokenRepository;
import com.samuksa.user.domain.userinfo.dto.request.UserInfoRequest;
import com.samuksa.user.domain.userinfo.dto.response.GetUserInfoResponse;
import com.samuksa.user.domain.userinfo.mapper.InfoMapper;
import com.samuksa.user.errorexception.entity.errorHandler.db.DbErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.db.DbException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.security.util.IOUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final InfoMapper infoMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustUserRepository custUserRepository;
    private final UserJwtTokenRepository userJwtTokenRepository;
    private final UserImageRepository userImageRepository;
    @Value("${image.upload.path}")
    private String uploadPath;
    @Value("${image.default.path}")
    private String defaultPath;

    public void deleteUserInfo(UserInfoRequest userInfoRequest){
        CustUser custUser = custUserRepository.findByuserId(userInfoRequest.getUserId());
        checkVaildUser(userInfoRequest, custUser);
        infoMapper.deleteUserInfo(custUser.getUserId());
    }

    public GetUserInfoResponse getUserInfo(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CustUser custUser = custUserRepository.findByuserId(userId);
        UserImage userImage = userImageRepository.findByCustUser_userId(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userImage.getProfilePath() == null)
            userImage.setProfilePath(defaultPath + File.separator + "defaultprofileimage.png");
        File file = new File(userImage.getProfilePath());
        try {
            return GetUserInfoResponse.builder()
                    .userId(custUser.getUserId())
                    .email(custUser.getEmail())
                    .nickName(custUser.getNickName())
                    .profileImage(FileUtil.readAsByteArray(file))
                    .build();
        }
        catch (IOException e){
            return null;
        }
    }

    public void patchUserInfo(UserInfoRequest userInfoRequest){
        CustUser custUser = custUserRepository.findByuserId(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userInfoRequest.getNewPassword() != null)
            custUser.setUserPassword(bCryptPasswordEncoder.encode(userInfoRequest.getNewPassword()));
        if (userInfoRequest.getNewNickName() != null)
            custUser.setNickName(userInfoRequest.getNewNickName());
        custUserRepository.save(custUser);
    }

    public ResponseEntity<String> uploadImage(List<MultipartFile> multipartFiles){
        if (multipartFiles.size() != 1)
            return ResponseEntity.badRequest().body("많은 이미지");
        for(MultipartFile image : multipartFiles){
            if (image.getContentType().startsWith("image") == false)
                return ResponseEntity.badRequest().body("이미지가 아님");

            String originName = image.getOriginalFilename();
            String fileName = originName.substring(originName.lastIndexOf("."));
            String userId = makeFolder();
            String saveName = uploadPath + File.separator + userId + File.separator + "profile" + fileName;
            Path savePath = Paths.get(saveName);
            try {
                image.transferTo(savePath);
            }
            catch (IOException e){
                return ResponseEntity.badRequest().body("IOException");
            }
            UserImage userImage = userImageRepository.findByCustUser_userId(userId);
            if (userImage.getProfilePath() != null)
                new File(userImage.getProfilePath()).delete();
            userImage.setProfilePath(saveName);
            userImageRepository.save(userImage);
        }
        return ResponseEntity.ok("success");
    }

    private String makeFolder() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        File uploadPatheFolder = new File(uploadPath, userId);
        if (uploadPatheFolder.exists() == false)
            uploadPatheFolder.mkdirs();

        return userId;
    }
    private void checkVaildUser(UserInfoRequest userInfoRequest, CustUser custUser){
        if (custUser == null)
            throw new DbException("No Id", DbErrorCode.ID_REGISTERED);
        if (userInfoRequest != null && !bCryptPasswordEncoder.matches(userInfoRequest.getPassword(), custUser.getUserPassword()))
            throw new DbException("Not Match PassWord", DbErrorCode.PW_NOT_MATCH);
    }
}
