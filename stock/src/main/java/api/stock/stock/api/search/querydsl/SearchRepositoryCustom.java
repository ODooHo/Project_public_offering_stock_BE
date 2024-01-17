package api.stock.stock.api.search.querydsl;

import api.stock.stock.api.search.SearchEntity;

import java.util.List;

public interface SearchRepositoryCustom {
    List<SearchEntity> findRecent(String userEmail, String category);

}


