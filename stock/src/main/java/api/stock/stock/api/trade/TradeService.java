package api.stock.stock.api.trade;

import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    public ResponseDto<TradeEntity> createTrade(TradeDto dto){
        String tradeName = dto.getTradeName();
        try{
            if (tradeRepository.existsByTradeName(tradeName)){
                return ResponseDto.setFailed("Trade already Exist!");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        TradeEntity trade = modelMapper.map(dto,TradeEntity.class);
        
        try{
            tradeRepository.save(trade);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return ResponseDto.setSuccess("Success",trade);
    }

    public ResponseDto<String> deleteTrade(String userEmail, Integer tradeId){
        TradeEntity trade = tradeRepository.findById(tradeId).orElse(null);
        String tradeUserEmail = trade.getUserEmail();

        if(!userEmail.equals(tradeUserEmail)){
            return ResponseDto.setFailed("Wrong Request(userEmail doesn't Match)");
        }

        try{
            tradeRepository.deleteById(tradeId);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return ResponseDto.setSuccess("Success","Delete Completed");
    }

    @Transactional(readOnly = true)
    public ResponseDto<List<TradeEntity>> getTradeList(String userEmail){
        List<TradeEntity> tradeList = new ArrayList<>();

        try{
            tradeList = tradeRepository.findByUserEmail(userEmail);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return ResponseDto.setSuccess("Success",tradeList);
    }


    public void deleteByWithdraw(String userEmail){
        try{
            tradeRepository.deleteAllByUserEmail(userEmail);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }



}
