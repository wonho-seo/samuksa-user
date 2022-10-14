package com.samuksa.user.domain.board.freeboard.controller;

import com.samuksa.user.domain.board.freeboard.dto.request.BoardCreateRequest;
import com.samuksa.user.domain.board.freeboard.dto.request.GetTitleRequest;
import com.samuksa.user.domain.board.freeboard.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardService freeBoardService;

    @PostMapping("/create")
    public ResponseEntity<?> createBoard(final @RequestBody BoardCreateRequest boardCreateRequest){
        return freeBoardService.createBoard(boardCreateRequest);
    }

    @GetMapping
    public ResponseEntity<?> getTitle(final @RequestBody GetTitleRequest getTitleRequest){
        return freeBoardService.getTitle(getTitleRequest);
    }
    @GetMapping("/comments")
    public ResponseEntity<?> getContents(final long idx){
        return freeBoardService.getContents(idx);
    }
}
