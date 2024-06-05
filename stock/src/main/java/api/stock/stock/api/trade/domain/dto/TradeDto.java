package api.stock.stock.api.trade.domain.dto;

import api.stock.stock.api.trade.domain.entity.TradeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TradeDto {
    private Integer tradeId;
    private String userEmail;
    private LocalDate tradeDate;
    private String tradeName;
    private Integer buyPrice;
    private Integer sellPrice;
    private Integer tradeQuantity;
    private Integer tradeFee;
    private String memo;


    public static TradeDto of(
            Integer tradeId,
            String userEmail,
            LocalDate tradeDate,
            String tradeName,
            Integer buyPrice,
            Integer sellPrice,
            Integer tradeQuantity,
            Integer tradeFee,
            String memo) {
        return new TradeDto(tradeId, userEmail, tradeDate, tradeName, buyPrice, sellPrice, tradeQuantity, tradeFee, memo);
    }

    public static TradeDto from(TradeEntity entity){
        return TradeDto.of(
                entity.getTradeId(),
                entity.getUserEmail(),
                entity.getTradeDate(),
                entity.getTradeName(),
                entity.getBuyPrice(),
                entity.getSellPrice(),
                entity.getTradeQuantity(),
                entity.getTradeFee(),
                entity.getMemo()
        );
    }
}
