package api.stock.stock.api.trade.controller;

import api.stock.stock.api.trade.domain.dto.TradeDto;
import api.stock.stock.api.trade.domain.entity.TradeEntity;
import api.stock.stock.api.trade.service.TradeService;
import api.stock.stock.global.response.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/myPage/trade")
public class TradeController {

    private final TradeService tradeService;

    @Autowired
    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping("/createTrade")
    ResponseDto<TradeEntity> createTrade(@RequestBody TradeDto requestBody){
        return tradeService.createTrade(requestBody);
    }

    @GetMapping("/getTrade")
    ResponseDto<List<TradeEntity>> getTrade(@AuthenticationPrincipal String userEmail){
        return tradeService.getTradeList(userEmail);
    }

    @DeleteMapping("/deleteTrade/{tradeId}")
    ResponseDto<String>deleteTrade(@AuthenticationPrincipal String userEmail, @PathVariable Integer tradeId){
        return tradeService.deleteTrade(userEmail, tradeId);
    }


}
