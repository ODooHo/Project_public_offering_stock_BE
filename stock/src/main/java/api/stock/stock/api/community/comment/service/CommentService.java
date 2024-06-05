package api.stock.stock.api.community.comment.service;

import api.stock.stock.api.community.board.service.BoardService;
import api.stock.stock.api.community.comment.domain.dto.CommentDto;
import api.stock.stock.api.community.comment.domain.entity.CommentEntity;
import api.stock.stock.api.community.comment.domain.dto.PatchCommentDto;
import api.stock.stock.api.community.comment.domain.dto.PatchCommentResponseDto;
import api.stock.stock.api.community.comment.repository.CommentRepository;
import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final BoardService boardService;

    public CommentService(CommentRepository commentRepository, ModelMapper modelMapper, BoardService boardService) {

        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.boardService = boardService;
    }

    @Transactional(readOnly = true)
    public ResponseDto<List<CommentEntity>> getComment(Integer boardId) {
        List<CommentEntity> commentList = commentRepository.findByBoardId(boardId);
        return ResponseDto.setSuccess("Success", commentList);
    }

    public ResponseDto<CommentEntity> writeComment(Integer boardId, CommentDto dto) {
        CommentEntity comment = modelMapper.map(dto, CommentEntity.class);
        comment.setCommentWriteDate(LocalDate.now());
        boardService.increaseComment(boardId);
        commentRepository.save(comment);
        return ResponseDto.setSuccess("Success", comment);
    }

    public ResponseDto<PatchCommentResponseDto> patchComment(String userEmail, Integer commentId, PatchCommentDto dto) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.COMMENT_NOT_FOUND, String.format("Not exists Comment"))
        );
        String commentUserEmail = comment.getCommentWriterEmail();
        if (!userEmail.equals(commentUserEmail)) {
            throw new IPOApplicationException(ErrorCode.INVALID_PERMISSION, "InValid Permission");
        }
        comment.setCommentContent(dto.getCommentContent());
        comment.setCommentWriteDate(LocalDate.now());
        commentRepository.save(comment);

        PatchCommentResponseDto response = new PatchCommentResponseDto(comment);

        return ResponseDto.setSuccess("Success", response);
    }

    public ResponseDto<Void> deleteComment(String userEmail, Integer commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.COMMENT_NOT_FOUND, "Not exists Comment")
        );
        String commentWriterEmail = comment.getCommentWriterEmail();

        if (!commentWriterEmail.equals(userEmail)) {
            throw new IPOApplicationException(ErrorCode.INVALID_PERMISSION, "InValid Permission");
        }

        commentRepository.deleteById(commentId);

        return ResponseDto.setSuccess();
    }

    public ResponseDto<Void> deleteByBoard(Integer boardId) {
        commentRepository.deleteAllByBoardId(boardId);
        return ResponseDto.setSuccess();
    }

    public ResponseDto<Void> deleteByWithdraw(String userEmail) {
        commentRepository.deleteAllByCommentWriterEmail(userEmail);
        return ResponseDto.setSuccess();
    }


}
