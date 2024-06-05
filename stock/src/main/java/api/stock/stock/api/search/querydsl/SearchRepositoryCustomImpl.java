package api.stock.stock.api.search.querydsl;

import api.stock.stock.api.search.QSearchEntity;
import api.stock.stock.api.search.domain.entity.SearchEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class SearchRepositoryCustomImpl implements SearchRepositoryCustom {
    @PersistenceContext
    EntityManager em;

    private final JPAQueryFactory queryFactory;
    private final QSearchEntity qSearchEntity = QSearchEntity.searchEntity;

    public SearchRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<SearchEntity> findRecent(String userEmail, String category) {
        return queryFactory
                .selectFrom(qSearchEntity)
                .where(qSearchEntity.userEmail.eq(userEmail))
                .where(qSearchEntity.category.eq(category))
                .orderBy(qSearchEntity.searchId.desc())
                .limit(5)
                .fetch();
    }

}
