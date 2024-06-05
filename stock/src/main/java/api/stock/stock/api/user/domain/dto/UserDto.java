package api.stock.stock.api.user.domain.dto;

import api.stock.stock.api.user.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
    private String userEmail;
    private String userPassword;
    private String userNickname;
    private String userPhoneNumber;
    private String userProfile;


    public static UserDto of(String userEmail, String userPassword, String userNickname, String userPhoneNumber, String userProfile){
        return new UserDto(userEmail,userPassword,userNickname,userPhoneNumber,userProfile);
    }

    public static UserDto from(UserEntity entity){
        return UserDto.of(
                entity.getUserEmail(),
                entity.getUserPassword(),
                entity.getUserNickname(),
                entity.getUserPhoneNumber(),
                entity.getUserProfile()
        );
    }
}
