package api.stock.stock.api.community.board.domain.dto;

import api.stock.stock.api.community.board.domain.entity.BoardEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PatchBoardResponseDto {
    private BoardEntity board;
}
