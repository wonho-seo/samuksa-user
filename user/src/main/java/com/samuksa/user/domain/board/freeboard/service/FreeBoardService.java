package com.samuksa.user.domain.board.freeboard.service;


import com.samuksa.user.db.table.board.entity.BoardContents;
import com.samuksa.user.db.table.board.entity.BoardTitle;
import com.samuksa.user.db.table.board.repository.BoardCommentRepository;
import com.samuksa.user.db.table.board.repository.BoardContentsRepository;
import com.samuksa.user.db.table.board.repository.BoardTitleRepository;
import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import com.samuksa.user.domain.board.freeboard.dto.request.BoardCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FreeBoardService {
    private final BoardCommentRepository boardCommentRepository;
    private final BoardContentsRepository boardContentsRepository;
    private final BoardTitleRepository boardTitleRepository;
    private final CustUserRepository custUserRepository;

    public ResponseEntity<?> createBoard(BoardCreateRequest boardCreateRequest){
        CustUser custUser = custUserRepository.findByuserId(SecurityContextHolder.getContext().getAuthentication().getName());
        BoardTitle boardTitle = BoardTitle.builder()
                .userIdx(custUser.getUserIdx())
                .type(boardCreateRequest.getType())
                .title(boardCreateRequest.getTitle())
                .cTime(Timestamp.valueOf(LocalDateTime.now()))
                .mTime(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        BoardContents boardContents = BoardContents.builder()
                .boardTitle(boardTitle)
                .contents(boardCreateRequest.getText())
                .build();
        boardContentsRepository.save(boardContents);
        return ResponseEntity.ok("create");
    }
}
