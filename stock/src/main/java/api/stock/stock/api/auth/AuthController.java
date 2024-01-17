package api.stock.stock.api.auth;

import api.stock.stock.api.app.DeleteApplication;
import api.stock.stock.api.user.UserEntity;
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
    public ResponseDto<UserEntity> signUp(@RequestBody SignUpDto requestBody){
        return authService.signUp(requestBody);
    }

    @PostMapping("/signUp/emailCheck/{userEmail}")
    public ResponseDto<String> emailCheck(@PathVariable String userEmail){
        return authService.emailCheck(userEmail);
    }

    @PostMapping("/signUp/nicknameCheck/{userNickname}")
    public ResponseDto<String> nicknameCheck(@PathVariable String userNickname){
        return authService.nicknameCheck(userNickname);
    }


    @PostMapping("/signIn")
    public ResponseDto<SignInResponseDto> signIn(@RequestBody SignInDto requestBody){
        return authService.signIn(requestBody);
    }

    @PostMapping("/logout")
    public ResponseDto<String> logout(@RequestHeader String token){
        return authService.logout(token);
    }

    @PostMapping("/getAccess")
    public ResponseDto<RefreshResponseDto> getAccess(@RequestHeader String refreshToken){
        return authService.getAccess(refreshToken);
    }

    @DeleteMapping("/withDraw")
    public ResponseDto<String> withDraw(@AuthenticationPrincipal String userEmail){
        return deleteApplication.widthDraw(userEmail);
    }

}
