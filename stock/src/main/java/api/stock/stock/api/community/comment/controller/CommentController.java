package api.stock.stock.api.community.comment.controller;

import api.stock.stock.api.community.comment.domain.dto.CommentDto;
import api.stock.stock.api.community.comment.domain.dto.PatchCommentDto;
import api.stock.stock.api.community.comment.domain.dto.PatchCommentResponseDto;
import api.stock.stock.api.community.comment.domain.entity.CommentEntity;
import api.stock.stock.api.community.comment.service.CommentService;
import api.stock.stock.global.response.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/board")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{boardId}/comment")
    ResponseDto<List<CommentEntity>> getComment(@PathVariable Integer boardId){
        return commentService.getComment(boardId);
    }

    @PostMapping("/{boardId}/writeComment")
    ResponseDto<CommentEntity> writeComment(@PathVariable Integer boardId, @RequestBody CommentDto requestBody){
        return commentService.writeComment(boardId,requestBody);
    }

    @PatchMapping("/{boardId}/edit/{commentId}")
    ResponseDto<PatchCommentResponseDto> patchComment(@AuthenticationPrincipal String userEmail, @PathVariable Integer commentId, @RequestBody PatchCommentDto requestBody){
        return commentService.patchComment(userEmail,commentId, requestBody);
    }

    @DeleteMapping("/{boardId}/delete/{commentId}")
    ResponseDto<Void> deleteComment(@AuthenticationPrincipal String userEmail, @PathVariable Integer commentId){
        return commentService.deleteComment(userEmail, commentId);
    }





}
