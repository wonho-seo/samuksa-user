package com.samuksa.user.domain.board.freeboard.dto.response;

import com.samuksa.user.db.table.board.entity.BoardComment;
import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import lombok.Getter;
import org.aspectj.util.FileUtil;
import org.springframework.data.domain.Page;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class GetCommentsResponse {
    private List<Comments> comments;
    private int pageSize;
    private int pageNumber;
    private int numberOfElements;
    private int totalPages;

    public GetCommentsResponse(Page<BoardComment> page, CustUserRepository custUserRepository){
        List<Comments> list = new ArrayList<>();
        for (BoardComment boardComment : page.getContent()){
            Optional<CustUser> custUser = custUserRepository.findById(boardComment.getUserIdx());
            list.add(new Comments(boardComment, custUser.get()));
        }
        this.pageSize = page.getSize();
        this.pageNumber = page.getNumber();
        this.numberOfElements = page.getNumberOfElements();
        this.totalPages = page.getTotalPages();
    }

     class Comments{
        private long idx;
        private long commentIdx;
        private String comments;
        private Timestamp mTime;
        private Timestamp cTime;
        private String nickName;
        private byte[] profileImage;

        Comments(BoardComment boardComment, CustUser custUser){
            this.idx = boardComment.getIdx();
            this.commentIdx = boardComment.getCommentIdx();
            this.comments = boardComment.getComment();
            this.mTime = boardComment.getMTime();
            this.cTime = boardComment.getCTime();
            this.nickName = custUser.getNickName();
            try {
                this.profileImage = FileUtil.readAsByteArray(new File(custUser.getProfileImagePath()));
            }
            catch (IOException e) {
                this.profileImage = null;
            }
        }
    }
}
