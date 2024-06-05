package api.stock.stock.api.search;

import api.stock.stock.api.community.board.domain.entity.BoardEntity;
import api.stock.stock.api.ipo.domain.entity.IpoEntity;
import api.stock.stock.api.search.domain.dto.SearchDto;
import api.stock.stock.api.search.repository.SearchRepository;
import api.stock.stock.api.search.service.SearchService;
import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Slf4j
class SearchServiceTest {
    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private SearchService searchService;

    @Test
    void searchBoard() {
        //given
        SearchDto dto = new SearchDto();
        dto.setUserEmail("1");
        dto.setSearchContent("Patch");

        //when
        ResponseDto<List<BoardEntity>> response = searchService.searchBoard(dto);
        //then
        assertThat(response.getMessage()).isEqualTo("Success");
        log.info("SearchBoardTest {}",response.getData());
    }

    @Test
    void searchIpo() {
        //given
        SearchDto dto = new SearchDto();
        dto.setUserEmail("1");
        dto.setSearchContent("퓨릿");
        //when
        ResponseDto<List<IpoEntity>> response = searchService.searchIpo(dto);
        //then
        assertThat(response.getMessage()).isEqualTo("Success");
        log.info("SearchIpoTest {}",response.getData());
    }
}