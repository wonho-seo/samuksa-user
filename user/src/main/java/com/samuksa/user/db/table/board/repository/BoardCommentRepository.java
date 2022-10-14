package com.samuksa.user.db.table.board.repository;

import com.samuksa.user.db.table.board.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    List<BoardComment> findByBoardContentsIdx(long boardContentsIdx);
    List<BoardComment> findByCommentIdx(long commentIdx);
}
