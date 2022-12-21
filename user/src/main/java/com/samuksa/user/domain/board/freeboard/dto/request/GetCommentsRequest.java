package com.samuksa.user.domain.board.freeboard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetCommentsRequest {
    private int boardTitleIdx;
    private int page;
    private int size;
}
