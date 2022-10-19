package com.samuksa.user.db.table.board.repository;

import com.samuksa.user.db.table.board.entity.BoardComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    Optional<Page<BoardComment>> findByBoardTitleIdx(long boardTitleIdx, Pageable pageable);

    void deleteByBoardTitleIdx(long titleIdx);
}
