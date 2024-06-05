package api.stock.stock.api.community.comment.domain.dto;

import api.stock.stock.api.community.comment.domain.entity.CommentEntity;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Integer commentId;
    private Integer boardId;
    private String commentWriterEmail;
    private String commentContent;
    private LocalDate commentWriteDate;
    private String commentWriterNickname;

    public static CommentDto of(
            Integer commentId,
            Integer boardId,
            String commentWriterEmail,
            String commentContent,
            LocalDate commentWriteDate,
            String commentWriterNickname
    ){
        return new CommentDto(commentId, boardId, commentWriterEmail, commentContent, commentWriteDate, commentWriterNickname);
    }

    public static CommentDto from(CommentEntity entity){
        return CommentDto.of(
                entity.getCommentId(),
                entity.getBoardId(),
                entity.getCommentWriterEmail(),
                entity.getCommentContent(),
                entity.getCommentWriteDate(),
                entity.getCommentWriterNickname()
        );
    }
}
