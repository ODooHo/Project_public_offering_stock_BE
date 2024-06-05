package api.stock.stock.api.community.comment.domain.dto;

import lombok.*;


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
    private String commentWriterNickname;
}
