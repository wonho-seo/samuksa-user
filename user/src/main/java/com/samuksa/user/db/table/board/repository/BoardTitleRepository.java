package com.samuksa.user.db.table.board.repository;

import com.samuksa.user.db.table.board.entity.BoardTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardTitleRepository extends JpaRepository<BoardTitle, Long> {
    Optional<List<BoardTitle>> findByUserIdx(long userIdx);
    Optional<List<BoardTitle>> findByType(int type);
    Optional<Page<BoardTitle>> findByType(int type, Pageable pageable);
}
