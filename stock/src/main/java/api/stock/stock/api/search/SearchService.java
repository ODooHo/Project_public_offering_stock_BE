package api.stock.stock.api.search;

import api.stock.stock.api.community.board.BoardEntity;
import api.stock.stock.api.community.board.BoardService;
import api.stock.stock.api.ipo.IpoEntity;
import api.stock.stock.api.ipo.IpoService;
import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public ResponseDto<List<SearchEntity>>getRecentBoard(String userEmail){
        List<SearchEntity> search = new ArrayList<>();
        try{
            search = searchRepository.findRecent(userEmail,"board");
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return ResponseDto.setSuccess("Success",search);
    }

    @Transactional(readOnly = true)
    public ResponseDto<List<SearchEntity>>getRecentIpo(String userEmail){
        List<SearchEntity> search = new ArrayList<>();
        try{
            search = searchRepository.findRecent(userEmail,"ipo");
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return ResponseDto.setSuccess("Success",search);
    }



    public ResponseDto<List<BoardEntity>> searchBoard(SearchDto dto){
        SearchEntity search = modelMapper.map(dto,SearchEntity.class);
        String userEmail = search.getUserEmail();
        String searchContent = search.getSearchContent();
        List<BoardEntity> board = null;
        try{
            search.setCategory("board");
            String searchWord = search.getSearchContent();
            board = boardService.searchBoard(searchWord);
            if (!searchRepository.existsByUserEmailAndSearchContent(userEmail,searchContent)){
                searchRepository.save(search);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }


        return ResponseDto.setSuccess("Success",board);
    }

    public ResponseDto<List<IpoEntity>> searchIpo(SearchDto dto){
        SearchEntity search = modelMapper.map(dto,SearchEntity.class);
        String userEmail = search.getUserEmail();
        String searchContent = search.getSearchContent();
        List<IpoEntity> ipo = null;
        try {
            search.setCategory("ipo");
            String searchWord = search.getSearchContent();
            ipo = ipoService.searchIpo(searchWord);
            if (!searchRepository.existsByUserEmailAndSearchContent(userEmail,searchContent)){
                searchRepository.save(search);
            }
        }catch (Exception e){
                throw new RuntimeException(e);
        }
        return ResponseDto.setSuccess("Success",ipo);

    }

    public ResponseDto<String> deleteSearchWord(Integer searchId){
        try{
            searchRepository.deleteById(searchId);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return ResponseDto.setSuccess("Success","Delete Completed");
    }


    public void deleteByWithdraw(String userEmail){
        try{
            searchRepository.deleteAllByUserEmail(userEmail);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }







}
