package api.stock.stock.api.community.likes.repository;

import api.stock.stock.api.community.likes.domain.entity.LikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LikesRepository extends JpaRepository<LikesEntity, Integer> {
    Integer countByBoardId(Integer boardId);
    void deleteByBoardIdAndUserEmail(Integer boardId, String userEmail);
    void deleteAllByBoardId(Integer boardId);
    void deleteAllByUserEmail(String userEmail);
    boolean existsByUserEmailAndBoardId(String userEmail, Integer BoardId);
}
