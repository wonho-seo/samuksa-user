package com.samuksa.user.db.table.board.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Builder
@Table(name = "board_contents", schema = "samuksa_board_db")
public class BoardContents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private long idx;

    @OneToOne
    @JoinColumn(name = "board_title_idx")
    private BoardTitle boardTitle;

    @Column(name = "contents")
    private String contents;
}
