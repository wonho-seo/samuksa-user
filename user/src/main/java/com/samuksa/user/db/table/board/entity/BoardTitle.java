package com.samuksa.user.db.table.board.entity;

import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "board_title", schema = "samuksa_board_db")
public class BoardTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private long idx;

    @Column(name = "user_idx")
    private long userIdx;

    @Column(name = "m_time")
    private Timestamp mTime;

    @Column(name = "c_time")
    private Timestamp cTime;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private int type;

    @Column(name = "recommend_number")
    private int recommendNumber;
}
