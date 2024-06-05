package api.stock.stock.api.ipo.domain.dto;

import api.stock.stock.api.ipo.domain.entity.IpoEntity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class IpoDto {
    @Id
    private String id;
    private String ipoName;
    private String ipoCode;
    private String owner;
    private String locate;
    private String seed;
    private String business;
    private String ipoQuantity;
    private String faceValue;
    private String collusion;
    private String chief;
    private String compete;
    private String commit;
    private String date;
    private String publicDate;
    private List<String> sale;
    private List<String> profit;
    private List<String> pureProfit;
    private String protect;
    private String protectPercent;
    private String possible;
    private String possiblePercent;
    private String sharedQuantity;
    private String finalCollusion;

    public static IpoDto of(
            String id,
            String ipoName,
            String ipoCode,
            String owner,
            String locate,
            String seed,
            String business,
            String ipoQuantity,
            String faceValue,
            String collusion,
            String chief,
            String compete,
            String commit,
            String date,
            String publicDate,
            List<String> sale,
            List<String> profit,
            List<String> pureProfit,
            String protect,
            String protectPercent,
            String possible,
            String possiblePercent,
            String sharedQuantity,
            String finalCollusion) {
        return new IpoDto(id, ipoName, ipoCode, owner, locate, seed, business, ipoQuantity, faceValue, collusion, chief, compete, commit, date, publicDate, sale, profit, pureProfit, protect, protectPercent, possible, possiblePercent, sharedQuantity, finalCollusion);
    }

    public static IpoDto from(IpoEntity entity) {
        return IpoDto.of(
                entity.getId(),
                entity.getIpoName(),
                entity.getIpoCode(),
                entity.getOwner(),
                entity.getLocate(),
                entity.getSeed(),
                entity.getBusiness(),
                entity.getIpoQuantity(),
                entity.getFaceValue(),
                entity.getCollusion(),
                entity.getChief(),
                entity.getCompete(),
                entity.getCommit(),
                entity.getDate(),
                entity.getPublicDate(),
                entity.getSale(),
                entity.getProfit(),
                entity.getPureProfit(),
                entity.getProtect(),
                entity.getProtectPercent(),
                entity.getPossible(),
                entity.getPossiblePercent(),
                entity.getSharedQuantity(),
                entity.getFinalCollusion()
        );
    }
}
