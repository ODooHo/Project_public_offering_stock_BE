package api.stock.stock.api.search;

import api.stock.stock.api.search.querydsl.SearchRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;



public interface SearchRepository extends JpaRepository<SearchEntity,Integer>, SearchRepositoryCustom {
    boolean existsByUserEmailAndSearchContent(String userEmail, String searchContent);
    void deleteAllByUserEmail(String userEmail);
}
