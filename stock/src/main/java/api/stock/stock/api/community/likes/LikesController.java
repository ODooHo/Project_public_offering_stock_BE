package api.stock.stock.api.community.likes;

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
    ResponseDto<String> deleteLike(@PathVariable Integer boardId, @AuthenticationPrincipal String userEmail){
        return likesService.deleteLike(boardId, userEmail);
    }


    @GetMapping("/{boardId}/likes/get/count")
    ResponseDto<Integer> getLikesCount(@PathVariable Integer boardId){
        return likesService.getLikesCount(boardId);
    }

}
