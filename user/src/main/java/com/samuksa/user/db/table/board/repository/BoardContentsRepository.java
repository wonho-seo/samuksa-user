package com.samuksa.user.db.table.board.repository;

import com.samuksa.user.db.table.board.entity.BoardContents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardContentsRepository extends JpaRepository<BoardContents, Long> {
    Optional<BoardContents> findByBoardTitleIdx(long boardTitleIdx);
    void deleteByBoardTitleIdx(long boardTitleIdx);
}
