package api.stock.stock.api.ipo.favor.domain.dto;

import api.stock.stock.api.ipo.favor.domain.entity.FavorEntity;
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



    public static FavorDto of(Integer favorId, String ipoName, String userEmail){
        return new FavorDto(favorId,ipoName,userEmail);
    }


    public static FavorDto from(FavorEntity entity){
        return FavorDto.of(
                entity.getFavorId(),
                entity.getIpoName(),
                entity.getUserEmail()
        );
    }

}


