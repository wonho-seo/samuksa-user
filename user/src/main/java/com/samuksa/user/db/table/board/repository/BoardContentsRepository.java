package com.samuksa.user.db.table.board.repository;

import com.samuksa.user.db.table.board.entity.BoardContents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardContentsRepository extends JpaRepository<BoardContents, Long> {
    List<BoardContents> findByBoardTitleIdx(long boardTitleIdx);
}
