package com.samuksa.user.domain.board.freeboard.controller;

import com.samuksa.user.domain.board.freeboard.dto.request.*;
import com.samuksa.user.domain.board.freeboard.dto.response.GetCommentsResponse;
import com.samuksa.user.domain.board.freeboard.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardService freeBoardService;

    @GetMapping
    public ResponseEntity<?> getTitle(final @RequestBody GetTitleRequest getTitleRequest){
        return freeBoardService.getTitle(getTitleRequest);
    }
    @PostMapping("/create/board")
    public ResponseEntity<?> createBoard(final @RequestBody BoardCreateRequest boardCreateRequest){
        return freeBoardService.createBoard(boardCreateRequest);
    }
    @PatchMapping("/create/board")
    public ResponseEntity<String> patchBoard(final @RequestBody PatchBoardRequest patchBoardRequest){
        return freeBoardService.patchTitle(patchBoardRequest);
    }
    @DeleteMapping("/create/board")
    public ResponseEntity<String> deleteBaord(final @RequestBody long titleIdx){
        return freeBoardService.deleteTitle(titleIdx);
    }
    @GetMapping("/contents")
    public ResponseEntity<?> getContents(final long idx){
        return freeBoardService.getContents(idx);
    }
    @PostMapping("/create/comments")
    public ResponseEntity<String> createComment(final @RequestBody CommentCreateRequest commentCreateRequest){
        return freeBoardService.creatComments(commentCreateRequest);
    }
    @PatchMapping("/create/comments")
    public ResponseEntity<String> patchComment(final @RequestBody PatchCommentRequest patchCommentRequest){
        return freeBoardService.patchComment(patchCommentRequest);
    }
    @GetMapping("/comments")
    public ResponseEntity<GetCommentsResponse> getComment(final @RequestBody GetCommentsRequest getCommentsRequest){
        return freeBoardService.getComments(getCommentsRequest);
    }
    @DeleteMapping("/comments")
    public ResponseEntity<String> deleteComment(final @RequestBody long commentsIdx){
        return freeBoardService.deleteComments(commentsIdx);
    }
}
