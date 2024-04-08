package api.stock.stock.api.community.board;

import api.stock.stock.api.app.DeleteApplication;
import api.stock.stock.api.file.FileService;
import api.stock.stock.global.response.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/community/board")
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final DeleteApplication deleteApplication;

    @Autowired
    public BoardController(BoardService boardService, FileService fileService, DeleteApplication deleteApplication) {
        this.boardService = boardService;
        this.fileService = fileService;
        this.deleteApplication = deleteApplication;
    }

    @PostMapping("/writeBoard")
    public ResponseDto<BoardEntity> register(
            @RequestParam("boardTitle") String boardTitle,
            @RequestParam("boardContent") String boardContent,
            @RequestParam("boardWriterEmail") String boardWriterEmail,
            @RequestParam("boardWriterProfile") String boardWriterProfile,
            @RequestParam("boardWriterNickname") String boardWriterNickname,
            @RequestParam(value = "boardImage", required = false) MultipartFile boardImage){
        return boardService.register(boardTitle, boardContent,boardWriterEmail,boardWriterProfile,boardWriterNickname,
                boardImage);
    }

    @GetMapping("/list")
    public ResponseDto<List<BoardEntity>> getList(){
        return boardService.getList();
    }

    @GetMapping("/{boardId}")
    public ResponseDto<BoardEntity> getBoard(@PathVariable Integer boardId){
        return boardService.getBoard(boardId);
    }

    @GetMapping("/{boardId}/image")
    public ResponseEntity<byte[]> getBoardImage(@PathVariable Integer boardId) throws IOException {
        return fileService.getBoardImage(boardId);
    }


    @DeleteMapping("/delete/{boardId}")
    public ResponseDto<Void> deleteBoard(@AuthenticationPrincipal String userEmail, @PathVariable Integer boardId){
        return deleteApplication.deleteBoard(userEmail,boardId);
    }

    @PatchMapping("/edit/{boardId}")
    public ResponseDto<PatchBoardResponseDto> patchBoard(@AuthenticationPrincipal String userEmail, @PathVariable Integer boardId, @RequestBody PatchBoardDto requestBody){
        return boardService.patchBoard(userEmail, boardId, requestBody);
    }

//    @GetMapping("/{boardId}")
//    public ResponseDto<?>increaseView(@PathVariable Integer boardId){
//        ResponseDto<?> result = boardService.increaseView(boardId);
//        return result;
//    }



}


