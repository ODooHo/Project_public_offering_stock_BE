package api.stock.stock.api.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshResponseDto {
    private String token;
    private Integer tokenExprTime;


    public static RefreshResponseDto of(String token, Integer tokenExprTime){
        return new RefreshResponseDto(token,tokenExprTime);
    }
}
