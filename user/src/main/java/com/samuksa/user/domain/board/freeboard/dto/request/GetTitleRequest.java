package com.samuksa.user.domain.board.freeboard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class GetTitleRequest {
    private int page;
    private int size;
    private int type;

}
