package api.stock.stock.api.ipo.favor.controller;

import api.stock.stock.api.ipo.domain.dto.IpoDto;
import api.stock.stock.api.ipo.domain.entity.IpoEntity;
import api.stock.stock.api.ipo.favor.domain.dto.FavorDto;
import api.stock.stock.api.ipo.favor.domain.entity.FavorEntity;
import api.stock.stock.api.ipo.favor.service.FavorService;
import api.stock.stock.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stock")
public class FavorController {

    private final FavorService favorService;


    @GetMapping("/getFavor")
    ResponseDto<List<IpoDto>> getFavorList(@AuthenticationPrincipal String userEmail){
        List<IpoDto> result = favorService.getFavorList(userEmail);
        return ResponseDto.setSuccess(result);
    }


    @PostMapping("/addFavor")
    ResponseDto<FavorDto> addFavor(@RequestBody FavorDto requestBody){
        FavorDto result = favorService.addFavor(requestBody);
        return ResponseDto.setSuccess(result);
    }

    @DeleteMapping("/{ipoName}/deleteFavor")
    ResponseDto<Void> deleteFavor(@AuthenticationPrincipal String userEmail, @PathVariable String ipoName){
        favorService.deleteFavor(userEmail,ipoName);
        return ResponseDto.setSuccess();
    }


}
