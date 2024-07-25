package api.stock.stock.api.community.comment.service;

import api.stock.stock.api.community.board.service.BoardService;
import api.stock.stock.api.community.comment.domain.dto.CommentDto;
import api.stock.stock.api.community.comment.domain.dto.PatchCommentDto;
import api.stock.stock.api.community.comment.domain.entity.CommentEntity;
import api.stock.stock.api.community.comment.repository.CommentRepository;
import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final BoardService boardService;


    @Transactional(readOnly = true)
    public List<CommentDto> getComment(Integer boardId) {
        return commentRepository.findByBoardId(boardId).stream().map(CommentDto::from).toList();
    }

    public CommentDto writeComment(Integer boardId, CommentDto dto) {
        CommentEntity comment = modelMapper.map(dto, CommentEntity.class);
        comment.setCommentWriteDate(LocalDate.now());
        boardService.increaseComment(boardId);
        commentRepository.save(comment);
        return CommentDto.from(comment);
    }

    public CommentDto patchComment(String userEmail, Integer commentId, PatchCommentDto dto) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.COMMENT_NOT_FOUND)
        );
        String commentUserEmail = comment.getCommentWriterEmail();
        if (!userEmail.equals(commentUserEmail)) {
            throw new IPOApplicationException(ErrorCode.INVALID_PERMISSION);
        }
        comment.setCommentContent(dto.getCommentContent());
        comment.setCommentWriteDate(LocalDate.now());
        commentRepository.save(comment);

        return CommentDto.from(comment);
    }

    public void deleteComment(String userEmail, Integer commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.COMMENT_NOT_FOUND, "Not exists Comment")
        );
        String commentWriterEmail = comment.getCommentWriterEmail();

        if (!commentWriterEmail.equals(userEmail)) {
            throw new IPOApplicationException(ErrorCode.INVALID_PERMISSION, "InValid Permission");
        }

        commentRepository.deleteById(commentId);

    }

    public void deleteByBoard(Integer boardId) {
        commentRepository.deleteAllByBoardId(boardId);
    }

    public void  deleteByWithdraw(String userEmail) {
        commentRepository.deleteAllByCommentWriterEmail(userEmail);
    }


}
