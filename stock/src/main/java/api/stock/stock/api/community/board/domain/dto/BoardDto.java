package api.stock.stock.api.community.board.domain.dto;

import api.stock.stock.api.community.board.domain.entity.BoardEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {
    private Integer boardId;
    private String boardTitle;
    private String boardContent;
    private String boardWriterEmail;
    private String boardWriterProfile;
    private String boardWriterNickname;
    private LocalDate boardWriteDate;
    private String boardImage;
    private int boardClickCount;
    private int boardLikeCount;
    private int boardCommentCount;


    public static BoardDto of(
            Integer boardId,
            String boardTitle,
            String boardContent,
            String boardWriterEmail,
            String boardWriterProfile,
            String boardWriterNickname,
            LocalDate boardWriteDate,
            String boardImage,
            int boardClickCount,
            int boardLikeCount,
            int boardCommentCount
            ){
        return new BoardDto(
                boardId,
                boardTitle,
                boardContent,
                boardWriterEmail,
                boardWriterProfile,
                boardWriterNickname,
                boardWriteDate,
                boardImage,
                boardClickCount,
                boardLikeCount,
                boardCommentCount
        );
    }

    public static BoardDto from(BoardEntity entity){
        return BoardDto.of(
                entity.getBoardId(),
                entity.getBoardTitle(),
                entity.getBoardContent(),
                entity.getBoardWriterEmail(),
                entity.getBoardWriterProfile(),
                entity.getBoardWriterNickname(),
                entity.getBoardWriteDate(),
                entity.getBoardImage(),
                entity.getBoardClickCount(),
                entity.getBoardLikeCount(),
                entity.getBoardCommentCount()
        );
    }
}
