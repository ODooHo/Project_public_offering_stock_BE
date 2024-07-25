package api.stock.stock.api.trade.service;

import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
import api.stock.stock.api.trade.domain.dto.TradeDto;
import api.stock.stock.api.trade.domain.entity.TradeEntity;
import api.stock.stock.api.trade.repository.TradeRepository;
import api.stock.stock.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class TradeService {
    private final TradeRepository tradeRepository;
    private final ModelMapper modelMapper;


    public TradeDto createTrade(TradeDto dto) {
        String tradeName = dto.getTradeName();
        if (tradeRepository.existsByTradeName(tradeName)) {
            throw new IPOApplicationException(ErrorCode.DUPLICATED_TRADE_NAME);
        }
        TradeEntity trade = modelMapper.map(dto, TradeEntity.class);
        tradeRepository.save(trade);
        return TradeDto.from(trade);
    }

    public void deleteTrade(String userEmail, Integer tradeId) {
        TradeEntity trade = tradeRepository.findById(tradeId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.TRADE_NOT_FOUND, String.format("trade Id is %s", tradeId))
        );
        String tradeUserEmail = trade.getUserEmail();

        if (!userEmail.equals(tradeUserEmail)) {
            throw new IPOApplicationException(ErrorCode.INVALID_PERMISSION);
        }
        tradeRepository.deleteById(tradeId);
    }

    @Transactional(readOnly = true)
    public List<TradeDto> getTradeList(String userEmail) {
        return tradeRepository.findByUserEmail(userEmail).stream().map(TradeDto::from).toList();
    }


    public void deleteByWithdraw(String userEmail) {
        tradeRepository.deleteAllByUserEmail(userEmail);
    }

}
