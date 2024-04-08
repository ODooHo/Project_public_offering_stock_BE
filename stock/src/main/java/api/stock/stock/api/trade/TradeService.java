package api.stock.stock.api.trade;

import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class TradeService {
    private final TradeRepository tradeRepository;
    private final ModelMapper modelMapper;

    public TradeService(TradeRepository tradeRepository, ModelMapper modelMapper) {
        this.tradeRepository = tradeRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseDto<TradeEntity> createTrade(TradeDto dto) {
        String tradeName = dto.getTradeName();
        if (tradeRepository.existsByTradeName(tradeName)) {
            throw new IPOApplicationException(ErrorCode.DUPLICATED_TRADE_NAME);
        }
        TradeEntity trade = modelMapper.map(dto, TradeEntity.class);
        tradeRepository.save(trade);
        return ResponseDto.setSuccess("Success", trade);
    }

    public ResponseDto<String> deleteTrade(String userEmail, Integer tradeId) {
        TradeEntity trade = tradeRepository.findById(tradeId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.TRADE_NOT_FOUND, String.format("trade Id is %s", tradeId))
        );
        String tradeUserEmail = trade.getUserEmail();

        if (!userEmail.equals(tradeUserEmail)) {
            throw new IPOApplicationException(ErrorCode.INVALID_PERMISSION);
        }
        tradeRepository.deleteById(tradeId);
        return ResponseDto.setSuccess("Success", "Delete Completed");
    }

    @Transactional(readOnly = true)
    public ResponseDto<List<TradeEntity>> getTradeList(String userEmail) {
        return ResponseDto.setSuccess("Success", tradeRepository.findByUserEmail(userEmail));
    }


    public void deleteByWithdraw(String userEmail) {
        tradeRepository.deleteAllByUserEmail(userEmail);
    }

}
