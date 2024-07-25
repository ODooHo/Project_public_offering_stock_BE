package api.stock.stock.api.community.board.controller;

import api.stock.stock.api.app.DeleteApplication;
import api.stock.stock.api.community.board.domain.dto.BoardDto;
import api.stock.stock.api.community.board.domain.dto.request.PatchBoardRequestDto;
import api.stock.stock.api.community.board.service.BoardService;
import api.stock.stock.api.file.FileService;
import api.stock.stock.global.response.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/board")
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final DeleteApplication deleteApplication;


    @PostMapping("/writeBoard")
    public ResponseDto<BoardDto> register(
            @RequestParam("boardTitle") String boardTitle,
            @RequestParam("boardContent") String boardContent,
            @RequestParam("boardWriterEmail") String boardWriterEmail,
            @RequestParam("boardWriterProfile") String boardWriterProfile,
            @RequestParam("boardWriterNickname") String boardWriterNickname,
            @RequestParam(value = "boardImage", required = false) MultipartFile boardImage){
        BoardDto result = boardService.register(boardTitle, boardContent, boardWriterEmail, boardWriterProfile, boardWriterNickname,
                boardImage);
        return ResponseDto.setSuccess(result);
    }

    @GetMapping("/list")
    public ResponseDto<List<BoardDto>> getList(){
        List<BoardDto> result = boardService.getList();
        return ResponseDto.setSuccess(result);
    }

    @GetMapping("/{boardId}")
    public ResponseDto<BoardDto> getBoard(@PathVariable Integer boardId){
        BoardDto result = boardService.getBoard(boardId);
        return ResponseDto.setSuccess(result);
    }

    @GetMapping("/{boardId}/image")
    public ResponseEntity<byte[]> getBoardImage(@PathVariable Integer boardId){
        return fileService.getBoardImage(boardId);
    }


    @DeleteMapping("/delete/{boardId}")
    public ResponseDto<Void> deleteBoard(@AuthenticationPrincipal String userEmail, @PathVariable Integer boardId){
        return deleteApplication.deleteBoard(userEmail,boardId);
    }

    @PatchMapping("/edit/{boardId}")
    public ResponseDto<BoardDto> patchBoard(@AuthenticationPrincipal String userEmail, @PathVariable Integer boardId, @RequestBody PatchBoardRequestDto requestBody){
        BoardDto result = boardService.patchBoard(userEmail, boardId, requestBody);
        return ResponseDto.setSuccess(result);
    }

//    @GetMapping("/{boardId}")
//    public ResponseDto<?>increaseView(@PathVariable Integer boardId){
//        ResponseDto<?> result = boardService.increaseView(boardId);
//        return result;
//    }



}


