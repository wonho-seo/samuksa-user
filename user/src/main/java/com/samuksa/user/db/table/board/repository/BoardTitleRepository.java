package com.samuksa.user.db.table.board.repository;

import com.samuksa.user.db.table.board.entity.BoardTitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardTitleRepository extends JpaRepository<BoardTitle, Long> {
    List<BoardTitle> findByUserIdx(long userIdx);
    List<BoardTitle> findByType(int type);
}
