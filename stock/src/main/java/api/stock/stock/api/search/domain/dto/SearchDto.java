package api.stock.stock.api.search.domain.dto;

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
}
