package api.stock.stock.api.community.board.domain.dto.request;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PatchBoardRequestDto {
    private String boardTitle;
    private String boardContent;
}
