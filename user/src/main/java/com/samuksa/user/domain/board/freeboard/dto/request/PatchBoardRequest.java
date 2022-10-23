package com.samuksa.user.domain.board.freeboard.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PatchBoardRequest {
    @NotNull
    private long titleIdx;
    private String title;
    private String text;
    @NotNull
    private int type;
}
