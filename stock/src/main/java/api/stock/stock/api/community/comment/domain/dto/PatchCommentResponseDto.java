package api.stock.stock.api.community.comment.domain.dto;

import api.stock.stock.api.community.comment.domain.entity.CommentEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PatchCommentResponseDto {
    private CommentEntity comment;
}
