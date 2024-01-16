package api.stock.stock.api.trade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface TradeRepository extends JpaRepository<TradeEntity, Integer> {
    List<TradeEntity> findByUserEmail(String userEmail);
    boolean existsByTradeName(String tradeName);

    void deleteAllByUserEmail(String userEmail);

}
