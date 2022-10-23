package com.samuksa.user.domain.board.freeboard.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PatchCommentRequest {
    @NotNull
    private long commentIdx;
    private String comment;
}
