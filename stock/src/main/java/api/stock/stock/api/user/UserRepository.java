package api.stock.stock.api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<UserEntity , String>{
    boolean existsByUserNickname(String userNickname);


}
