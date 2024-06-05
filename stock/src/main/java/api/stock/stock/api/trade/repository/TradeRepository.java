package api.stock.stock.api.trade.repository;

import api.stock.stock.api.trade.domain.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface TradeRepository extends JpaRepository<TradeEntity, Integer> {
    List<TradeEntity> findByUserEmail(String userEmail);
    boolean existsByTradeName(String tradeName);

    void deleteAllByUserEmail(String userEmail);

}
