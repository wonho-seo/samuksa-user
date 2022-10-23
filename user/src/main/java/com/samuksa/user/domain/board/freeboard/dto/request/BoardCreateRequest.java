package com.samuksa.user.domain.board.freeboard.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
public class BoardCreateRequest {
    private String title;
    private String content;
    private int type;
}
