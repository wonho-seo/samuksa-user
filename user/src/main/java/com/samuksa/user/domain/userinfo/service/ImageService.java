package com.samuksa.user.domain.userinfo.service;


import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.DocFlavor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {


    @Value("${image.upload.path}")
    private String uploadPath;
    @Value("${resources.path}")
    private String resourcesPath;
    @Value("${image.show.path}")
    private String showPath;

    private final CustUserRepository custUserRepository;

    public ResponseEntity<byte[]> showImage(String filename) throws MalformedURLException {
        String imagePath = resourcesPath + "/" + uploadPath + "/" + filename;
        File file = new File(imagePath);
        try {
            InputStream imageStream = new FileInputStream(imagePath);
            byte[] b = IOUtils.toByteArray(imageStream);
            imageStream.close();
            return ResponseEntity.ok(b);
//            return ResponseEntity.ok(FileUtil.readAsByteArray(file));
        }
        catch (IOException e){
            return null;
        }
    }

    public ResponseEntity<String> uploadImage(MultipartFile image){
        if (!image.getContentType().startsWith("image"))
            return ResponseEntity.badRequest().body("이미지가 아님");
        String originName = image.getOriginalFilename();
        String fileName = originName.substring(originName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String userId = makeFolder();
        String saveName =  resourcesPath + File.separator + uploadPath + File.separator + uuid + fileName;
        Path savePath = Paths.get(saveName);
        try {
            image.transferTo(savePath);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("IOException");
        }

        CustUser custUser = custUserRepository.findByuserId(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        if (custUser.getProfileImagePath() != null)
            new File(custUser.getProfileImagePath()).delete();
        custUser.setProfileImagePath(showPath + uuid + fileName);
        custUserRepository.save(custUser);

        return ResponseEntity.ok("success");
    }

    private String makeFolder() { // 유저이름의 폴더를 만들어 유저의 이미지들을 한곳에 모아서 관리한다.
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        File uploadPathFolder = new File(uploadPath, userId);
        if (!uploadPathFolder.exists())
            uploadPathFolder.mkdirs();

        return userId;
    }
}
