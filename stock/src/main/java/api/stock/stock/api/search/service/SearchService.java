package api.stock.stock.api.search.service;

import api.stock.stock.api.community.board.domain.dto.BoardDto;
import api.stock.stock.api.community.board.domain.entity.BoardEntity;
import api.stock.stock.api.community.board.service.BoardService;
import api.stock.stock.api.ipo.domain.dto.IpoDto;
import api.stock.stock.api.ipo.domain.entity.IpoEntity;
import api.stock.stock.api.ipo.service.IpoService;
import api.stock.stock.api.search.domain.dto.SearchDto;
import api.stock.stock.api.search.domain.entity.SearchEntity;
import api.stock.stock.api.search.repository.SearchRepository;
import api.stock.stock.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class SearchService {
    private final ModelMapper modelMapper;
    private final SearchRepository searchRepository;
    private final BoardService boardService;
    private final IpoService ipoService;




    @Transactional(readOnly = true)
    public List<SearchDto> getRecentBoard(String userEmail) {
        return searchRepository.findRecent(userEmail, "board").stream().map(SearchDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<SearchDto> getRecentIpo(String userEmail) {
        return searchRepository.findRecent(userEmail, "ipo").stream().map(SearchDto::from).toList();
    }


    public List<BoardDto> searchBoard(SearchDto dto) {
        List<BoardDto> boardList = boardService.searchBoard(dto.getSearchContent());
        if (!searchRepository.existsByUserEmailAndSearchContent(dto.getUserEmail(), dto.getSearchContent())) {
            SearchEntity search = modelMapper.map(dto, SearchEntity.class);
            search.setCategory("board");
            searchRepository.save(search);
        }
        return boardList;
    }

    public List<IpoDto> searchIpo(SearchDto dto) {
        List<IpoDto> ipoList = ipoService.searchIpo(dto.getSearchContent());
        if (!searchRepository.existsByUserEmailAndSearchContent(dto.getUserEmail(), dto.getSearchContent())) {
            SearchEntity search = modelMapper.map(dto, SearchEntity.class);
            search.setCategory("ipo");
            searchRepository.save(search);
        }
        return ipoList;

    }

    public void deleteSearchWord(Integer searchId) {
        searchRepository.deleteById(searchId);
    }


    public void deleteByWithdraw(String userEmail) {
        searchRepository.deleteAllByUserEmail(userEmail);
    }


}
