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
    public ResponseEntity<?> getTitle(final int page, final int size, final int type){
        GetTitleRequest getTitleRequest = new GetTitleRequest(page, size, type);
        return freeBoardService.getTitle(getTitleRequest);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createBoard(final @RequestBody BoardCreateRequest boardCreateRequest){
        return freeBoardService.createBoard(boardCreateRequest);
    }
    @PatchMapping("/create")
    public ResponseEntity<String> patchBoard(final @RequestBody PatchBoardRequest patchBoardRequest){
        return freeBoardService.patchTitle(patchBoardRequest);
    }
    @DeleteMapping("/create")
    public ResponseEntity<String> deleteBoard(final @RequestBody long titleIdx){
        return freeBoardService.deleteTitle(titleIdx);
    }
    @GetMapping("/contents")
    public ResponseEntity<?> getContents(final long idx){
        return freeBoardService.getContents(idx);
    }
    @PostMapping("/create/comments")
    public ResponseEntity<String> createComment(final @RequestBody CommentCreateRequest commentCreateRequest){
        return freeBoardService.createComments(commentCreateRequest);
    }
    @PatchMapping("/create/comments")
    public ResponseEntity<String> patchComment(final @RequestBody PatchCommentRequest patchCommentRequest){
        return freeBoardService.patchComment(patchCommentRequest);
    }
    @GetMapping("/comments")
    public ResponseEntity<GetCommentsResponse> getComment(final int boardTitleIdx, final int page, final int size){
        GetCommentsRequest getCommentsRequest = new GetCommentsRequest(boardTitleIdx, page, size);
        return freeBoardService.getComments(getCommentsRequest);
    }
    @DeleteMapping("/comments")
    public ResponseEntity<String> deleteComment(final @RequestBody long commentsIdx){
        return freeBoardService.deleteComments(commentsIdx);
    }
}
