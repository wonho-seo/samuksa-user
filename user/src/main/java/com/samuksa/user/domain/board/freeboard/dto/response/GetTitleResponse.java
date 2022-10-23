package com.samuksa.user.domain.board.freeboard.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samuksa.user.db.table.board.entity.BoardTitle;
import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.util.FileUtil;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
public class GetTitleResponse {
    private long idx;
    private String nickName;
    private String profileImage;
    private String mTime;
    private String cTime;
    private String title;
    private int type;
    private int recommendNumber;

    public GetTitleResponse(BoardTitle boardTitle, CustUserRepository custUserRepository){
        this.idx = boardTitle.getIdx();
        this.mTime = boardTitle.getMTime().toString();
        this.cTime = boardTitle.getCTime().toString();
        this.title = boardTitle.getTitle();
        this.type = boardTitle.getType();
        this.recommendNumber = boardTitle.getRecommendNumber();

        CustUser custUser = custUserRepository.findById(boardTitle.getUserIdx()).orElseThrow(() -> new UsernameNotFoundException("NonValid User"));
        this.nickName = custUser.getNickName();
        this.profileImage = custUser.getProfileImagePath();
    }
}
