package api.stock.stock.api.ipo.favor.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FavorDto {
    private Integer favorId;
    private String ipoName;
    private String userEmail;
}
