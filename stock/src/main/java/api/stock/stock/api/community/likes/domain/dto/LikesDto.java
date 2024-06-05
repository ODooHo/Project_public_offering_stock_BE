package api.stock.stock.api.community.likes.domain.dto;

import api.stock.stock.api.community.likes.domain.entity.LikesEntity;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LikesDto {
    private Integer likeId;
    private Integer boardId;
    private String userEmail;


    public static LikesDto of(Integer likeId, Integer boardId, String userEmail) {
        return new LikesDto(likeId, boardId, userEmail);
    }

    public static LikesDto from(LikesEntity entity) {
        return LikesDto.of(entity.getLikeId(), entity.getBoardId(), entity.getUserEmail());
    }
}
