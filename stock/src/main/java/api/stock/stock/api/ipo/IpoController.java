package api.stock.stock.api.ipo;

import api.stock.stock.global.response.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class IpoController {
    private final IpoService ipoService;

    @Autowired
    public IpoController(IpoService ipoService) {
        this.ipoService = ipoService;
    }


    @GetMapping("/list")
    ResponseDto<List<IpoEntity>>getIpoList(){
        return ipoService.getIpoList();
    }

    @GetMapping("/{ipoName}")
    ResponseDto<IpoEntity> getIpo(@PathVariable String ipoName){
        return ipoService.getIpo(ipoName);
    }

}
