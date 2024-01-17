package api.stock.stock.api.community.comment;

import api.stock.stock.api.community.board.BoardService;
import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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
        List<CommentEntity> commentList = new ArrayList<>();
        try{
            commentList = commentRepository.findByBoardId(boardId);
        }catch (Exception e){
            log.error("Database Error",e);
            return ResponseDto.setFailed("DataBase Error");
        }

        return ResponseDto.setSuccess("Success", commentList);
    }

    public ResponseDto<CommentEntity> writeComment(Integer boardId, CommentDto dto) {
        CommentEntity comment = modelMapper.map(dto,CommentEntity.class);
        comment.setCommentWriteDate(LocalDate.now());
        try{
            boardService.increaseComment(boardId);
            commentRepository.save(comment);
        }catch (Exception e){
            log.error("Database Error",e);
            return ResponseDto.setFailed("DataBase Error");
        }
        return ResponseDto.setSuccess("Success", comment);
    }

    public ResponseDto<PatchCommentResponseDto> patchComment(String userEmail,Integer commentId, PatchCommentDto dto){
        CommentEntity comment = commentRepository.findById(commentId).orElse(null);
        String commentUserEmail = comment.getCommentWriterEmail();
        if(!userEmail.equals(commentUserEmail)){
            return ResponseDto.setFailed("Wrong Request(userEmail doesn't Match)");
        }

        String commentContent = dto.getCommentContent();
        LocalDate date = LocalDate.now();

        try{
            comment.setCommentContent(commentContent);
            comment.setCommentWriteDate(date);
            commentRepository.save(comment);
        }catch (Exception e){
            log.error("Database Error",e);
            return ResponseDto.setFailed("DataBase Error");
        }

        PatchCommentResponseDto response = new PatchCommentResponseDto(comment);

        return ResponseDto.setSuccess("Success" , response);
    }

    public ResponseDto<String> deleteComment(String userEmail, Integer commentId){
        CommentEntity comment = commentRepository.findById(commentId).orElse(null);
        String commentWriterEmail = comment.getCommentWriterEmail();

        if (!commentWriterEmail.equals(userEmail)){
            return ResponseDto.setFailed("Wrong Request(userEmail doesn't Match)");
        }

        try{
            commentRepository.deleteById(commentId);
        }catch (Exception e){
            log.error("Database Error",e);
            return ResponseDto.setFailed("DataBase Error");
        }

        return ResponseDto.setSuccess("Success","Delete Completed");
    }

    public ResponseDto<String> deleteByBoard(Integer boardId){
        try{
            commentRepository.deleteAllByBoardId(boardId);
        }catch (Exception e){
            log.error("DataBase Error",e);
            return ResponseDto.setFailed("DataBase Error");
        }

        return ResponseDto.setSuccess("Success","Delete Completed");

    }
    public void deleteByWithdraw(String userEmail){
        try{
            commentRepository.deleteAllByCommentWriterEmail(userEmail);
        }catch (Exception e){
            log.error("DataBase Error",e);
        }

    }


}
