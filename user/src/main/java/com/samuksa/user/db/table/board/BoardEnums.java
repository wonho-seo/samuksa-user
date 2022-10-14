package com.samuksa.user.db.table.board;

import lombok.Getter;

@Getter
public enum BoardEnums {
    FreeBoard(1);

    private final int value;
    BoardEnums(int value){
        this.value = value;
    }
}
