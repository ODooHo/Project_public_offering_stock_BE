package api.stock.stock.api.user.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="user")
@Table(name="user")
public class UserEntity {
    @Id
    private String userEmail;
    private String userPassword;
    private String userNickname;
    private String userPhoneNumber;
    private String userProfile;


    public static UserEntity of(String userEmail, String userPassword, String userNickname, String userPhoneNumber, String userProfile){
        return new UserEntity(
                userEmail,
                userPassword,
                userNickname,
                userPhoneNumber,
                userProfile
        );
    }

}
