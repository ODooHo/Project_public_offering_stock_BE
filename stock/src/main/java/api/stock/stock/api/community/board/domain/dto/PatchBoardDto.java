package api.stock.stock.api.community.board.domain.dto;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PatchBoardDto {
    private String boardTitle;
    private String boardContent;
}
