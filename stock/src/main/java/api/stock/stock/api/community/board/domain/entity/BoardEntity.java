package api.stock.stock.api.community.board.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Board")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


    public static BoardEntity of(
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
            int boardCommentCount) {

        return new BoardEntity(
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

    public static BoardEntity of(
            String boardTitle,
            String boardContent,
            String boardWriterEmail,
            String boardWriterProfile,
            String boardWriterNickname,
            LocalDate boardWriteDate) {

        return BoardEntity.of(
                null,
                boardTitle,
                boardContent,
                boardWriterEmail,
                boardWriterProfile,
                boardWriterNickname,
                boardWriteDate,
                null,
                0,
                0,
                0
        );
    }

}
