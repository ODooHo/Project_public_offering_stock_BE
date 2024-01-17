package api.stock.stock.api.search;

import java.util.List;

public interface SearchRepositoryCustom {
    List<SearchEntity> findRecent(String userEmail, String category);

}


