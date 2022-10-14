package com.samuksa.user.db.table.board.repository;

import com.samuksa.user.db.table.board.entity.BoardTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardTitleRepository extends JpaRepository<BoardTitle, Long> {
    List<BoardTitle> findByUserId(String userId);
    List<BoardTitle> findByType(int type);
    Page<BoardTitle> findByType(int type, Pageable pageable);
}
