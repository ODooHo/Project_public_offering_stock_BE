package api.stock.stock.api.search;

import api.stock.stock.api.community.board.BoardEntity;
import api.stock.stock.api.ipo.IpoEntity;
import api.stock.stock.global.response.ResponseDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/community")
    public ResponseDto<List<SearchEntity>>getRecentBoard(@AuthenticationPrincipal String userEmail){
        return searchService.getRecentBoard(userEmail);
    }

    @GetMapping("/stock")
    public ResponseDto<List<SearchEntity>>getRecentIpo(@AuthenticationPrincipal String userEmail){
        return searchService.getRecentIpo(userEmail);
    }

    @PostMapping("/stock")
    public ResponseDto<List<IpoEntity>>searchIpo(@RequestBody SearchDto requestBody){
        return searchService.searchIpo(requestBody);
    }

    @PostMapping("/community")
    public ResponseDto<List<BoardEntity>>searchBoard(@RequestBody SearchDto requestBody){
        return searchService.searchBoard(requestBody);
    }

    @DeleteMapping("/{searchId}/delete")
    public ResponseDto<Void>deleteSearchWord(@PathVariable Integer searchId){
        return searchService.deleteSearchWord(searchId);
    }


}
