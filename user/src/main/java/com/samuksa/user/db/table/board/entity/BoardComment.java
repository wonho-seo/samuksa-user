package com.samuksa.user.db.table.board.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@Table(name = "board_comment", schema = "samuksa_board_db")
public class BoardComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private long idx;

    @Column(name = "board_title_idx")
    private long boardTitleIdx;

    @Column(name = "comment_idx")
    private long commentIdx;

    @Column(name = "comment")
    private String comment;

    @Column(name = "m_time")
    private Timestamp mTime;

    @Column(name = "c_time")
    private Timestamp cTime;
}
