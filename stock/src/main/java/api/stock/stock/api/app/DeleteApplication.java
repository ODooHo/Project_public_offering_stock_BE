package api.stock.stock.api.app;

import api.stock.stock.api.community.board.BoardService;
import api.stock.stock.api.community.comment.CommentService;
import api.stock.stock.api.community.likes.LikesService;
import api.stock.stock.api.file.FileService;
import api.stock.stock.api.ipo.favor.FavorService;
import api.stock.stock.api.search.SearchService;
import api.stock.stock.api.trade.TradeService;
import api.stock.stock.api.user.UserService;
import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeleteApplication {

    private final BoardService boardService;
    private final CommentService commentService;
    private final LikesService likesService;
    private final FileService fileService;
    private final FavorService favorService;
    private final TradeService tradeService;
    private final SearchService searchService;
    private final UserService userService;


    public DeleteApplication(BoardService boardService, CommentService commentService, LikesService likesService,
                                  FileService fileService, FavorService favorService, TradeService tradeService,
                                  SearchService searchService, UserService userService) {
        this.boardService = boardService;
        this.commentService = commentService;
        this.likesService = likesService;
        this.fileService = fileService;
        this.favorService = favorService;
        this.tradeService = tradeService;
        this.searchService = searchService;
        this.userService = userService;
    }


    public ResponseDto<String> deleteBoard(String userEmail, Integer boardId){
        try{
            fileService.deleteBoardImage(boardId);
            commentService.deleteByBoard(boardId);
            likesService.deleteByBoard(boardId);
            boardService.deleteBoard(userEmail,boardId);
        }catch (Exception e){
            return ResponseDto.setFailed("Database Error");
        }
        return ResponseDto.setSuccess("delete Completed","");
    }

    public ResponseDto<String> widthDraw(String userEmail){
        try{
            boardService.deleteByWithdraw(userEmail);
            commentService.deleteByWithdraw(userEmail);
            likesService.deleteByWithdraw(userEmail);
            favorService.deleteByWithdraw(userEmail);
            tradeService.deleteByWithdraw(userEmail);
            searchService.deleteByWithdraw(userEmail);
            userService.withDraw(userEmail);
        }catch (Exception e){
            log.error("Database Error",e);
            return ResponseDto.setFailed("DataBase Error");
        }

        return ResponseDto.setSuccess("Success", "Delete Completed");
    }

}
