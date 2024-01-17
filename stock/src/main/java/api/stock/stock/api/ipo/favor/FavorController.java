package api.stock.stock.api.ipo.favor;

import api.stock.stock.api.ipo.IpoEntity;
import api.stock.stock.global.response.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class FavorController {

    private final FavorService favorService;

    @Autowired
    public FavorController(FavorService favorService) {
        this.favorService = favorService;
    }

    @GetMapping("/getFavor")
    ResponseDto<List<IpoEntity>> getFavorList(@AuthenticationPrincipal String userEmail){
        return favorService.getFavorList(userEmail);
    }


    @PostMapping("/addFavor")
    ResponseDto<FavorEntity> addFavor(@RequestBody FavorDto requestBody){
        return favorService.addFavor(requestBody);
    }

    @DeleteMapping("/{ipoName}/deleteFavor")
    ResponseDto<String> deleteFavor(@AuthenticationPrincipal String userEmail, @PathVariable String ipoName){
        return favorService.deleteFavor(userEmail,ipoName);
    }


}
