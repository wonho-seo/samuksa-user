package com.samuksa.user.domain.board.freeboard.dto.request;

import lombok.Getter;

@Getter
public class GetCommentsRequest {
    private int boardTitleIdx;
    private int page;
    private int size;
}
