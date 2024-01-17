package api.stock.stock.api.community.likes;

import api.stock.stock.api.community.board.BoardEntity;
import api.stock.stock.api.community.board.BoardService;
import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@Slf4j
public class LikesService {
    private final LikesRepository likesRepository;
    private final BoardService boardService;
    private final ModelMapper modelMapper;


    public LikesService(LikesRepository likesRepository, BoardService boardService, ModelMapper modelMapper) {
        this.likesRepository = likesRepository;
        this.boardService = boardService;
        this.modelMapper = modelMapper;
    }

    public ResponseDto<LikesEntity> addLike(LikesDto dto){
        LikesEntity like= modelMapper.map(dto, LikesEntity.class);
        try{
            boolean isLiked = likesRepository.existsByUserEmailAndBoardId(like.getUserEmail(), like.getBoardId());
            if (isLiked) {
                return ResponseDto.setFailed("Already Liked");
            }
            Integer boardId = like.getBoardId();
            boardService.increaseLike(boardId);
            likesRepository.save(like);
        }catch (Exception e){
            log.error("Database Error",e);
            return ResponseDto.setFailed("DataBase Error");
        }

        return ResponseDto.setSuccess("Success",like);
    }

    public ResponseDto<String> deleteLike(Integer boardId, String userEmail) {
        try{
            // 데이터베이스에서 사용자의 닉네임과 게시글 번호에 해당하는 좋아요 삭제
            likesRepository.deleteByBoardIdAndUserEmail(boardId, userEmail);
            // 해당 게시글의 좋아요 개수 감소
            boardService.decreaseLike(boardId);
        }catch (Exception e){
            log.error("Database Error",e);
            return ResponseDto.setFailed("DataBase Error");
        }

        return ResponseDto.setSuccess("Success","Delete Completed");
    }



    @Transactional(readOnly = true)
    public ResponseDto<Integer> getLikesCount(Integer boardId){
        Integer count = 0;
        Integer check;
        BoardEntity board = null;
        try{
            check = boardService.getLikeCount(boardId);
            count = likesRepository.countByBoardId(boardId);
            if(count!=check){
                boardService.updateLike(boardId,count);
            }

        }catch (Exception e){
            log.error("Database Error",e);
            return ResponseDto.setFailed("DataBase Error");
        }

        return ResponseDto.setSuccess("Success",count);
    }

    public void deleteByBoard(Integer boardId){
        try{
            likesRepository.deleteAllByBoardId(boardId);
        }catch (Exception e){
            log.error("DataBase Error",e);
        }

    }

    public void deleteByWithdraw(String userEmail){
        try{
            likesRepository.deleteAllByUserEmail(userEmail);
        }catch (Exception e){
            log.error("DataBase Error",e);
        }
    }


}
