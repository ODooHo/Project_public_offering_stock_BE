package api.stock.stock.api.search.repository;

import api.stock.stock.api.search.domain.entity.SearchEntity;
import api.stock.stock.api.search.querydsl.SearchRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SearchRepository extends JpaRepository<SearchEntity,Integer>, SearchRepositoryCustom {
    boolean existsByUserEmailAndSearchContent(String userEmail, String searchContent);
    void deleteAllByUserEmail(String userEmail);
}
