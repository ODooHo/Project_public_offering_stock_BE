package api.stock.stock.api.auth.dto.response;

import api.stock.stock.api.user.domain.dto.UserDto;
import api.stock.stock.api.user.domain.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponseDto {
    private String token;
    private Integer tokenExprTIme;
    private String refreshToken;
    private Integer refreshExprTime;
    private UserDto user;

    public static SignInResponseDto of(String token, Integer tokenExprTIme, String refreshToken, Integer refreshExprTime, UserDto user){
        return new SignInResponseDto(
                token,
                tokenExprTIme,
                refreshToken,
                refreshExprTime,
                user
        );
    }
}
