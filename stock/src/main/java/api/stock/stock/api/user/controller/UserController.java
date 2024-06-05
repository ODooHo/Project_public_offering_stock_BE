package api.stock.stock.api.user.controller;

import api.stock.stock.api.file.FileService;
import api.stock.stock.api.user.domain.dto.UserDto;
import api.stock.stock.api.user.service.UserService;
import api.stock.stock.global.response.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/myPage")
public class UserController {

    private final UserService userService;
    private final FileService fileService;

    @Autowired
    public UserController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }


    @GetMapping("/profile")
    public ResponseEntity<byte[]> getProfile(@AuthenticationPrincipal String userEmail) {
        return fileService.getProfileImage(userEmail);
    }


    @PatchMapping("/patchUser")
    public ResponseDto<UserDto> patchUser(
            @RequestParam(value = "userNickname", required = false) String userNickname,
            @RequestParam(value = "userProfile",required = false) MultipartFile userProfile,
            @AuthenticationPrincipal String userEmail){
        UserDto result = userService.patchUser(userNickname, userProfile, userEmail);
        return ResponseDto.setSuccess(result);
    }

    @DeleteMapping("/profile/delete")
    public ResponseDto<Void> deleteProfile(@AuthenticationPrincipal String userEmail){
        return fileService.deleteProfileImage(userEmail);
    }



}
