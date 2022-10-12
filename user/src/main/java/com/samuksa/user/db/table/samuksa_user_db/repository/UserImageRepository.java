package com.samuksa.user.db.table.samuksa_user_db.repository;


import com.samuksa.user.db.table.samuksa_user_db.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage,Long> {
    UserImage findByCustUser_userId(String userId);
}
