package api.stock.stock.api.auth.controller;

import api.stock.stock.api.app.DeleteApplication;
import api.stock.stock.api.auth.dto.request.SignInDto;
import api.stock.stock.api.auth.dto.request.SignUpDto;
import api.stock.stock.api.auth.dto.response.RefreshResponseDto;
import api.stock.stock.api.auth.dto.response.SignInResponseDto;
import api.stock.stock.api.auth.service.AuthService;
import api.stock.stock.api.user.domain.dto.UserDto;
import api.stock.stock.api.user.domain.entity.UserEntity;
import api.stock.stock.global.response.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final DeleteApplication deleteApplication;

    @Autowired
    public AuthController(AuthService authService, DeleteApplication deleteApplication) {
        this.authService = authService;
        this.deleteApplication = deleteApplication;
    }

    @PostMapping("/signUp")
    public ResponseDto<UserDto> signUp(@RequestBody SignUpDto requestBody){
        UserDto result = authService.signUp(requestBody);
        return ResponseDto.setSuccess(result);
    }

    @PostMapping("/signUp/emailCheck/{userEmail}")
    public ResponseDto<Void> emailCheck(@PathVariable String userEmail){
        authService.emailCheck(userEmail);
        return ResponseDto.setSuccess();
    }

    @PostMapping("/signUp/nicknameCheck/{userNickname}")
    public ResponseDto<Void> nicknameCheck(@PathVariable String userNickname){
        authService.nicknameCheck(userNickname);
        return ResponseDto.setSuccess();
    }


    @PostMapping("/signIn")
    public ResponseDto<SignInResponseDto> signIn(@RequestBody SignInDto requestBody){
        SignInResponseDto result = authService.signIn(requestBody);
        return ResponseDto.setSuccess(result);
    }

    @PostMapping("/logout")
    public ResponseDto<Void> logout(@RequestHeader String token){
        authService.logout(token);
        return ResponseDto.setSuccess();
    }

    @PostMapping("/getAccess")
    public ResponseDto<RefreshResponseDto> getAccess(@RequestHeader String refreshToken){
        RefreshResponseDto result = authService.getAccess(refreshToken);
        return ResponseDto.setSuccess(result);
    }

    @DeleteMapping("/withDraw")
    public ResponseDto<Void> withDraw(@AuthenticationPrincipal String userEmail){
        deleteApplication.widthDraw(userEmail);
        return ResponseDto.setSuccess();
    }

}
