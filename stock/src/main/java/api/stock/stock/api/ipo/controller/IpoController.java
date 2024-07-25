package api.stock.stock.api.ipo.controller;

import api.stock.stock.api.ipo.domain.dto.IpoDto;
import api.stock.stock.api.ipo.domain.entity.IpoEntity;
import api.stock.stock.api.ipo.service.IpoService;
import api.stock.stock.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stock")
public class IpoController {
    private final IpoService ipoService;



    @GetMapping("/list")
    ResponseDto<List<IpoDto>>getIpoList(){
        List<IpoDto> result = ipoService.getIpoList();
        return ResponseDto.setSuccess(result);
    }

    @GetMapping("/{ipoName}")
    ResponseDto<IpoDto> getIpo(@PathVariable String ipoName){
        IpoDto result = ipoService.getIpo(ipoName);
        return ResponseDto.setSuccess(result);
    }

}
