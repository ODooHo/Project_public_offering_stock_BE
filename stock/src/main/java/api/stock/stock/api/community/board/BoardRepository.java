package api.stock.stock.api.community.board;

import api.stock.stock.api.community.board.querydsl.BoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface BoardRepository extends JpaRepository<BoardEntity, Integer>, BoardRepositoryCustom {
    List<BoardEntity> findByBoardTitleContains(String boardTitle);
    List<BoardEntity> findByBoardWriterEmail(String userEmail);
}
