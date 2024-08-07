package api.stock.stock.api.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInDto {
    @NotBlank
    private String userEmail;
    @NotBlank
    private String userPassword;


}
