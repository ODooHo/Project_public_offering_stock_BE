package api.stock.stock.api.community.comment.controller;

import api.stock.stock.api.community.comment.domain.dto.CommentDto;
import api.stock.stock.api.community.comment.domain.dto.PatchCommentDto;
import api.stock.stock.api.community.comment.service.CommentService;
import api.stock.stock.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/community/board")
public class CommentController {
    private final CommentService commentService;


    @GetMapping("/{boardId}/comment")
    ResponseDto<List<CommentDto>> getComment(@PathVariable Integer boardId){
        List<CommentDto> result = commentService.getComment(boardId);
        return ResponseDto.setSuccess(result);
    }

    @PostMapping("/{boardId}/writeComment")
    ResponseDto<CommentDto> writeComment(@PathVariable Integer boardId, @RequestBody CommentDto requestBody){
        CommentDto result = commentService.writeComment(boardId, requestBody);
        return ResponseDto.setSuccess(result);
    }

    @PatchMapping("/{boardId}/edit/{commentId}")
    ResponseDto<CommentDto> patchComment(@AuthenticationPrincipal String userEmail, @PathVariable Integer commentId, @RequestBody PatchCommentDto requestBody){
        CommentDto result = commentService.patchComment(userEmail, commentId, requestBody);
        return ResponseDto.setSuccess(result);
    }

    @DeleteMapping("/{boardId}/delete/{commentId}")
    ResponseDto<Void> deleteComment(@AuthenticationPrincipal String userEmail, @PathVariable Integer commentId){
        commentService.deleteComment(userEmail, commentId);
        return ResponseDto.setSuccess();
    }





}
