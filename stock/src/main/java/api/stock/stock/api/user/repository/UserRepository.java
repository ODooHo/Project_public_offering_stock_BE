package api.stock.stock.api.user.repository;

import api.stock.stock.api.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String>{
    boolean existsByUserNickname(String userNickname);


}
