package com.samuksa.user.domain.board.freeboard.service;


import com.samuksa.user.db.table.board.entity.BoardComment;
import com.samuksa.user.db.table.board.entity.BoardContents;
import com.samuksa.user.db.table.board.entity.BoardTitle;
import com.samuksa.user.db.table.board.repository.BoardCommentRepository;
import com.samuksa.user.db.table.board.repository.BoardContentsRepository;
import com.samuksa.user.db.table.board.repository.BoardTitleRepository;
import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import com.samuksa.user.domain.board.freeboard.dto.request.BoardCreateRequest;
import com.samuksa.user.domain.board.freeboard.dto.request.CommentCreateRequest;
import com.samuksa.user.domain.board.freeboard.dto.request.GetTitleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .userId(SecurityContextHolder.getContext().getAuthentication().getName())
                .type(boardCreateRequest.getType())
                .title(boardCreateRequest.getTitle())
                .cTime(Timestamp.valueOf(LocalDateTime.now()))
                .mTime(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        boardTitle = boardTitleRepository.saveAndFlush(boardTitle);
        BoardContents boardContents = BoardContents.builder()
                .boardTitle(boardTitle)
                .contents(boardCreateRequest.getText())
                .build();
        boardContentsRepository.save(boardContents);
        return ResponseEntity.ok("create");
    }

    public ResponseEntity<?> getTitle(GetTitleRequest getTitleRequest){
        Pageable pageable = PageRequest.of(getTitleRequest.getPage(), getTitleRequest.getSize(), Sort.by(Sort.Direction.DESC, "cTime"));
        Page<BoardTitle> page = boardTitleRepository.findByType(getTitleRequest.getType(), pageable);
        return ResponseEntity.ok(page);
    }

    public ResponseEntity<?> creatContents(CommentCreateRequest commentCreateRequest){
        if (!boardCommentRepository.existsById(commentCreateRequest.getCommendIdx()) || boardTitleRepository.existsById(commentCreateRequest.getTitleIdx()))
            return ResponseEntity.badRequest().body("nonValid idx");
        BoardComment boardComment = BoardComment.builder()
                .commentIdx(commentCreateRequest.getCommendIdx())
                .comment(commentCreateRequest.getComment())
                .boardTitleIdx(commentCreateRequest.getTitleIdx())
                .cTime(Timestamp.valueOf(LocalDateTime.now()))
                .mTime(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        boardCommentRepository.save(boardComment);
        return ResponseEntity.ok("success");
    }
    public ResponseEntity<?> getContents(long idx){
        BoardContents boardContents = boardContentsRepository.findByBoardTitleIdx(idx);
        return ResponseEntity.ok(boardContents.getContents());
    }
}
