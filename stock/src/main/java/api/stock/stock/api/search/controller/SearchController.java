package api.stock.stock.api.search.controller;

import api.stock.stock.api.community.board.domain.dto.BoardDto;
import api.stock.stock.api.community.board.domain.entity.BoardEntity;
import api.stock.stock.api.ipo.domain.dto.IpoDto;
import api.stock.stock.api.ipo.domain.entity.IpoEntity;
import api.stock.stock.api.search.domain.dto.SearchDto;
import api.stock.stock.api.search.domain.entity.SearchEntity;
import api.stock.stock.api.search.service.SearchService;
import api.stock.stock.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;


    @GetMapping("/community")
    public ResponseDto<List<SearchDto>> getRecentBoard(@AuthenticationPrincipal String userEmail) {
        List<SearchDto> result = searchService.getRecentBoard(userEmail);
        return ResponseDto.setSuccess(result);
    }

    @GetMapping("/stock")
    public ResponseDto<List<SearchDto>> getRecentIpo(@AuthenticationPrincipal String userEmail) {
        List<SearchDto> result = searchService.getRecentIpo(userEmail);
        return ResponseDto.setSuccess(result);
    }

    @PostMapping("/stock")
    public ResponseDto<List<IpoDto>> searchIpo(@RequestBody SearchDto requestBody) {
        List<IpoDto> result = searchService.searchIpo(requestBody);
        return ResponseDto.setSuccess(result);
    }

    @PostMapping("/community")
    public ResponseDto<List<BoardDto>> searchBoard(@RequestBody SearchDto requestBody) {
        List<BoardDto> result = searchService.searchBoard(requestBody);
        return ResponseDto.setSuccess(result);
    }

    @DeleteMapping("/{searchId}/delete")
    public ResponseDto<Void> deleteSearchWord(@PathVariable Integer searchId) {
        searchService.deleteSearchWord(searchId);
        return ResponseDto.setSuccess();
    }


}
