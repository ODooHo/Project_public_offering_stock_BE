package api.stock.stock.api.user.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserDto {
    private String userNickname;
    private String userProfile;
}
