package api.stock.stock.api.search.service;

import api.stock.stock.api.community.board.domain.entity.BoardEntity;
import api.stock.stock.api.community.board.service.BoardService;
import api.stock.stock.api.ipo.domain.entity.IpoEntity;
import api.stock.stock.api.ipo.service.IpoService;
import api.stock.stock.api.search.domain.dto.SearchDto;
import api.stock.stock.api.search.domain.entity.SearchEntity;
import api.stock.stock.api.search.repository.SearchRepository;
import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class SearchService {
    private final ModelMapper modelMapper;
    private final SearchRepository searchRepository;
    private final BoardService boardService;
    private final IpoService ipoService;


    public SearchService(ModelMapper modelMapper, SearchRepository searchRepository, BoardService boardService, IpoService ipoService) {
        this.modelMapper = modelMapper;
        this.searchRepository = searchRepository;
        this.boardService = boardService;
        this.ipoService = ipoService;
    }

    @Transactional(readOnly = true)
    public ResponseDto<List<SearchEntity>> getRecentBoard(String userEmail) {
        return ResponseDto.setSuccess("Success", searchRepository.findRecent(userEmail, "board"));
    }

    @Transactional(readOnly = true)
    public ResponseDto<List<SearchEntity>> getRecentIpo(String userEmail) {
        return ResponseDto.setSuccess("Success", searchRepository.findRecent(userEmail, "ipo"));
    }


    public ResponseDto<List<BoardEntity>> searchBoard(SearchDto dto) {
        List<BoardEntity> board = boardService.searchBoard(dto.getSearchContent());
        if (!searchRepository.existsByUserEmailAndSearchContent(dto.getUserEmail(), dto.getSearchContent())) {
            SearchEntity search = modelMapper.map(dto, SearchEntity.class);
            search.setCategory("board");
            searchRepository.save(search);
        }
        return ResponseDto.setSuccess("Success", board);
    }

    public ResponseDto<List<IpoEntity>> searchIpo(SearchDto dto) {
        List<IpoEntity> ipo = ipoService.searchIpo(dto.getSearchContent());
        if (!searchRepository.existsByUserEmailAndSearchContent(dto.getUserEmail(), dto.getSearchContent())) {
            SearchEntity search = modelMapper.map(dto, SearchEntity.class);
            search.setCategory("ipo");
            searchRepository.save(search);
        }
        return ResponseDto.setSuccess("Success", ipo);

    }

    public ResponseDto<Void> deleteSearchWord(Integer searchId) {
        searchRepository.deleteById(searchId);
        return ResponseDto.setSuccess();
    }


    public void deleteByWithdraw(String userEmail) {
        searchRepository.deleteAllByUserEmail(userEmail);
    }


}
