package com.samuksa.user.domain.board.freeboard.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {
    private long titleIdx;
    private long commendIdx;
    private String comment;
}
