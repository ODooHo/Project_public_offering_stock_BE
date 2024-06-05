package api.stock.stock.api.community.board.querydsl;

import api.stock.stock.api.community.board.domain.entity.BoardEntity;
import api.stock.stock.api.community.board.QBoardEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;


public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {
    @PersistenceContext
    EntityManager em;

    private JPAQueryFactory queryFactory;
    private final QBoardEntity qBoardEntity = QBoardEntity.boardEntity;

    public BoardRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<BoardEntity> findList(){
        return queryFactory
                .selectFrom(qBoardEntity)
                .orderBy(qBoardEntity.boardWriteDate.desc())
                .orderBy(qBoardEntity.boardId.desc())
                .fetch();
    }
}
