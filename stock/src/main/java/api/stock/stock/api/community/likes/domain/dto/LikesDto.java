package api.stock.stock.api.community.likes.domain.dto;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LikesDto{
    private Integer likeId;
    private Integer boardId;
    private String userEmail;
}
