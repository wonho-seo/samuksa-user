package com.samuksa.user.domain.board.freeboard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class GetTitleRequest {
    private int page;
    private int size;
    private int type;

}
