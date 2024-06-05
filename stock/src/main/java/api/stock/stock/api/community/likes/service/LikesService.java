package api.stock.stock.api.community.likes.service;

import api.stock.stock.api.community.board.service.BoardService;
import api.stock.stock.api.community.likes.domain.dto.LikesDto;
import api.stock.stock.api.community.likes.domain.entity.LikesEntity;
import api.stock.stock.api.community.likes.repository.LikesRepository;
import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
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

    public LikesDto addLike(LikesDto dto) {
        boolean isLiked = likesRepository.existsByUserEmailAndBoardId(dto.getUserEmail(), dto.getBoardId());
        if (isLiked) {
            throw new IPOApplicationException(ErrorCode.DUPLICATED_LIKED, String.format("user email is %s", dto.getUserEmail()));
        }
        LikesEntity like = modelMapper.map(dto, LikesEntity.class);
        Integer boardId = like.getBoardId();
        boardService.increaseLike(boardId);
        likesRepository.save(like);
        return LikesDto.from(like);
    }

    public ResponseDto<Void> deleteLike(Integer boardId, String userEmail) {
        // 데이터베이스에서 사용자의 닉네임과 게시글 번호에 해당하는 좋아요 삭제
        likesRepository.deleteByBoardIdAndUserEmail(boardId, userEmail);
        // 해당 게시글의 좋아요 개수 감소
        boardService.decreaseLike(boardId);
        return ResponseDto.setSuccess();
    }


    @Transactional
    public void updateLikesCount(Integer boardId) {
        int check = boardService.getLikeCount(boardId);
        int count = likesRepository.countByBoardId(boardId);
        if (count != check) {
            boardService.updateLike(boardId, count);
        }
    }

    public void deleteByBoard(Integer boardId) {
        likesRepository.deleteAllByBoardId(boardId);
    }

    public void deleteByWithdraw(String userEmail) {
        likesRepository.deleteAllByUserEmail(userEmail);
    }


}
