package api.stock.stock.api.community.board.querydsl;

import api.stock.stock.api.community.board.domain.entity.BoardEntity;

import java.util.List;

public interface BoardRepositoryCustom {
    List<BoardEntity> findList();
}
