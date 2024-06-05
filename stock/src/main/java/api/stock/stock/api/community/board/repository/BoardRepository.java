package api.stock.stock.api.community.board.repository;

import api.stock.stock.api.community.board.domain.entity.BoardEntity;
import api.stock.stock.api.community.board.querydsl.BoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer>, BoardRepositoryCustom {
    List<BoardEntity> findByBoardTitleContains(String boardTitle);
    List<BoardEntity> findByBoardWriterEmail(String userEmail);
}
