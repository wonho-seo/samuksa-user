package com.samuksa.user.domain.board.freeboard.service;


import com.samuksa.user.db.table.board.entity.BoardComment;
import com.samuksa.user.db.table.board.entity.BoardContents;
import com.samuksa.user.db.table.board.entity.BoardTitle;
import com.samuksa.user.db.table.board.repository.BoardCommentRepository;
import com.samuksa.user.db.table.board.repository.BoardContentsRepository;
import com.samuksa.user.db.table.board.repository.BoardTitleRepository;
import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import com.samuksa.user.domain.board.freeboard.dto.request.*;
import com.samuksa.user.domain.board.freeboard.dto.response.GetCommentsResponse;
import com.samuksa.user.domain.board.freeboard.dto.response.GetTitleResponse;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.tools.web.BadHttpRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
        CustUser custUser = custUserRepository.findByuserId(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException("Not Found User"));
        BoardTitle boardTitle = BoardTitle.builder()
                .userIdx(custUser.getUserIdx())
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

    public ResponseEntity<String> patchTitle(PatchBoardRequest patchBoardRequest) {
        BoardTitle boardTitle = boardTitleRepository.findById(patchBoardRequest.getTitleIdx()).orElseThrow(() -> new EntityNotFoundException());
        CustUser custUser = custUserRepository.findByuserId(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException("Not Found User"));
        if (custUser.getUserIdx() != boardTitle.getUserIdx())
            return ResponseEntity.badRequest().body("Not Your Board");
        if (patchBoardRequest.getTitle() != null) {
            boardTitle.setTitle(patchBoardRequest.getTitle());
        }
        if (patchBoardRequest.getText() != null) {
            BoardContents boardContents = boardContentsRepository.findByBoardTitleIdx(patchBoardRequest.getTitleIdx()).orElseThrow(() -> new EntityNotFoundException());
            boardContents.setContents(patchBoardRequest.getText());
            boardContentsRepository.save(boardContents);
        }
        boardTitle.setMTime(Timestamp.valueOf(LocalDateTime.now()));
        boardTitleRepository.save(boardTitle);
        return ResponseEntity.ok("success");
    }

    public ResponseEntity<?> getTitle(GetTitleRequest getTitleRequest){
        Pageable pageable = PageRequest.of(getTitleRequest.getPage(), getTitleRequest.getSize(), Sort.by(Sort.Direction.DESC, "cTime"));
        Page<BoardTitle> page = boardTitleRepository.findByType(getTitleRequest.getType(), pageable).orElseThrow(() -> new EntityNotFoundException());

        GetTitleResponse getTitleResponse = new GetTitleResponse(page, custUserRepository);
        return ResponseEntity.ok(getTitleResponse);
    }

    public ResponseEntity<String> deleteTitle(long titleIdx){
        BoardTitle boardTitle = boardTitleRepository.findById(titleIdx).orElseThrow(() -> new EntityNotFoundException());
        CustUser custUser = custUserRepository.findByuserId(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException());
        if (boardTitle.getUserIdx() != custUser.getUserIdx())
            return ResponseEntity.badRequest().body("Not Your Board");
        boardContentsRepository.deleteByBoardTitleIdx(titleIdx);
        boardCommentRepository.deleteByBoardTitleIdx(titleIdx);
        boardTitleRepository.deleteById(titleIdx);
        return ResponseEntity.ok("success");
    }
    public ResponseEntity<?> getContents(long idx){
        BoardContents boardContents = boardContentsRepository.findByBoardTitleIdx(idx).orElseThrow(()->new EntityNotFoundException());
        return ResponseEntity.ok(boardContents.getContents());
    }

    public ResponseEntity<String> creatComments(CommentCreateRequest commentCreateRequest){
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

    public ResponseEntity<String> patchComment(PatchCommentRequest patchCommentRequest){
        BoardComment boardComment = boardCommentRepository.findById(patchCommentRequest.getCommentIdx()).orElseThrow(() -> new EntityNotFoundException());
        CustUser custUser = custUserRepository.findByuserId(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        if (boardComment.getUserIdx() != custUser.getUserIdx())
            return ResponseEntity.badRequest().body("Not User's Board");
        boardComment.setComment(patchCommentRequest.getComment());
        boardComment.setMTime(Timestamp.valueOf(LocalDateTime.now()));
        boardCommentRepository.save(boardComment);
        return ResponseEntity.ok("success");
    }
    public ResponseEntity<GetCommentsResponse> getComments(GetCommentsRequest getCommentsRequest){
        Pageable pageable = PageRequest.of(getCommentsRequest.getPage(), getCommentsRequest.getSize(), Sort.by("comment_idx"));
        Page<BoardComment> page = boardCommentRepository.findByBoardTitleIdx(getCommentsRequest.getBoardTitleIdx(), pageable).orElseThrow(() -> new EntityNotFoundException());

        GetCommentsResponse getCommentsResponse = new GetCommentsResponse(page, custUserRepository);
        return ResponseEntity.ok(getCommentsResponse);
    }

    public ResponseEntity<String> deleteComments(long idx){
        BoardComment boardComment = boardCommentRepository.findById(idx).orElseThrow(()-> (new IllegalArgumentException("Comment's not have or already delete")));
        CustUser custUser = custUserRepository.findByuserId(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        if (custUser.getUserIdx() != boardComment.getUserIdx())
            throw new IllegalArgumentException("not user's Comments");
        boardComment.setComment("삭제된 댓글");
        boardComment.setUserIdx(0);
        boardCommentRepository.save(boardComment);
        return ResponseEntity.ok("success");
    }
}

