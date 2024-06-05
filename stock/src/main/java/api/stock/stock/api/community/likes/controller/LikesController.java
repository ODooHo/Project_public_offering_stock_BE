package api.stock.stock.api.community.likes.controller;

import api.stock.stock.api.community.likes.domain.dto.LikesDto;
import api.stock.stock.api.community.likes.domain.entity.LikesEntity;
import api.stock.stock.api.community.likes.service.LikesService;
import api.stock.stock.global.response.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/board")
public class LikesController {
    private final LikesService likesService;

    @Autowired
    public LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    @PostMapping("/{boardId}/likes/add")
    ResponseDto<LikesDto> addLike(@RequestBody LikesDto requestBody){
        LikesDto result = likesService.addLike(requestBody);
        return ResponseDto.setSuccess(result);
    }

    @DeleteMapping("/{boardId}/likes/delete")
    ResponseDto<Void> deleteLike(@PathVariable Integer boardId, @AuthenticationPrincipal String userEmail){
        likesService.deleteLike(boardId, userEmail);
        return ResponseDto.setSuccess();
    }


    @GetMapping("/{boardId}/likes/get/count")
    ResponseDto<Void> UpdateLikesCount(@PathVariable Integer boardId){
        likesService.updateLikesCount(boardId);
        return ResponseDto.setSuccess();
    }

}
