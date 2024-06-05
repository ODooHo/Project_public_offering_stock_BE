package api.stock.stock.api.community.comment.repository;

import api.stock.stock.api.community.comment.domain.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findByBoardId(Integer boardId);

    void deleteAllByBoardId(Integer boardId);

    void deleteAllByCommentWriterEmail(String userEmail);
}