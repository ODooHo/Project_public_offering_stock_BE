package api.stock.stock.api.search.domain.dto;

import api.stock.stock.api.search.domain.entity.SearchEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {
    private Integer searchId;
    private String searchContent;
    private String userEmail;
    private String category;

    public static SearchDto of(Integer searchId, String searchContent, String userEmail, String category){
        return new SearchDto(searchId,searchContent,userEmail,category);
    }

    public static SearchDto from(SearchEntity entity){
        return SearchDto.of(entity.getSearchId(),entity.getSearchContent(),entity.getUserEmail(),entity.getCategory());
    }
}
