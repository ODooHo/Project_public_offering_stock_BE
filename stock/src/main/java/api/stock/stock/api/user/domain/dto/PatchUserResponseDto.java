package api.stock.stock.api.user.domain.dto;

import api.stock.stock.api.user.domain.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserResponseDto {
    private UserEntity user;
}
