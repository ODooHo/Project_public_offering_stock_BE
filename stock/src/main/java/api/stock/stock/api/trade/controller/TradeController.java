package api.stock.stock.api.trade.controller;

import api.stock.stock.api.trade.domain.dto.TradeDto;
import api.stock.stock.api.trade.domain.entity.TradeEntity;
import api.stock.stock.api.trade.service.TradeService;
import api.stock.stock.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/myPage/trade")
public class TradeController {

    private final TradeService tradeService;


    @PostMapping("/createTrade")
    ResponseDto<TradeDto> createTrade(@RequestBody TradeDto requestBody){
        TradeDto result = tradeService.createTrade(requestBody);
        return ResponseDto.setSuccess(result);
    }

    @GetMapping("/getTrade")
    ResponseDto<List<TradeDto>> getTrade(@AuthenticationPrincipal String userEmail){
        List<TradeDto> result = tradeService.getTradeList(userEmail);
        return ResponseDto.setSuccess(result);
    }

    @DeleteMapping("/deleteTrade/{tradeId}")
    ResponseDto<Void>deleteTrade(@AuthenticationPrincipal String userEmail, @PathVariable Integer tradeId){
        tradeService.deleteTrade(userEmail, tradeId);
        return ResponseDto.setSuccess();
    }


}
