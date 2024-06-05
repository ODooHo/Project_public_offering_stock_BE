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
    ResponseDto<LikesEntity> addLike(@RequestBody LikesDto requestBody){
        return likesService.addLike(requestBody);
    }

    @DeleteMapping("/{boardId}/likes/delete")
    ResponseDto<Void> deleteLike(@PathVariable Integer boardId, @AuthenticationPrincipal String userEmail){
        return likesService.deleteLike(boardId, userEmail);
    }


    @GetMapping("/{boardId}/likes/get/count")
    ResponseDto<Void> UpdateLikesCount(@PathVariable Integer boardId){
        return likesService.updateLikesCount(boardId);
    }

}
