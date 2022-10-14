package com.samuksa.user.domain.board.freeboard.controller;

import com.samuksa.user.domain.board.freeboard.dto.request.BoardCreateRequest;
import com.samuksa.user.domain.board.freeboard.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardService freeBoardService;

    @PostMapping("/create")
    public ResponseEntity<?> createBoard(final @RequestBody BoardCreateRequest boardCreateRequest){
        return freeBoardService.createBoard(boardCreateRequest);
    }
}
