package api.stock.stock.api.auth.service;

import api.stock.stock.api.auth.dto.response.RefreshResponseDto;
import api.stock.stock.api.auth.dto.request.SignInDto;
import api.stock.stock.api.auth.dto.response.SignInResponseDto;
import api.stock.stock.api.auth.dto.request.SignUpDto;
import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
import api.stock.stock.api.user.domain.entity.UserEntity;
import api.stock.stock.global.response.ResponseDto;
import api.stock.stock.global.security.TokenProvider;
import api.stock.stock.api.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public AuthService(UserRepository userRepository, TokenProvider tokenProvider, ModelMapper modelMapper, RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.modelMapper = modelMapper;
        this.redisTemplate = redisTemplate;
    }

    public ResponseDto<UserEntity> signUp(SignUpDto dto) {
        UserEntity userEntity = modelMapper.map(dto, UserEntity.class);

        String encodedPassword = passwordEncoder.encode(dto.getUserPassword());
        userEntity.setUserPassword(encodedPassword);
        userRepository.save(userEntity);
        return ResponseDto.setSuccess("Success", userEntity);
    }

    public ResponseDto<String> emailCheck(String userEmail) {
        if (userRepository.existsById(userEmail)) {
            throw new IPOApplicationException(ErrorCode.DUPLICATED_USER_EMAIL);
        }
        return ResponseDto.setSuccess("Success", "available Email");
    }

    public ResponseDto<String> nicknameCheck(String userNickname) {
        if (userRepository.existsByUserNickname(userNickname)) {
            throw new IPOApplicationException(ErrorCode.DUPLICATED_USER_NICKNAME);
        }
        return ResponseDto.setSuccess("Success", "available Nickname");
    }


    @Transactional(readOnly = true)
    public ResponseDto<SignInResponseDto> signIn(SignInDto dto) {
        String userEmail = dto.getUserEmail();
        String userPassword = dto.getUserPassword();

        UserEntity userEntity = userRepository.findById(userEmail).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.USER_NOT_FOUND)
        );
        if (!passwordEncoder.matches(userPassword, userEntity.getUserPassword())) {
            throw new IPOApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        userEntity.setUserPassword("");

        String token = tokenProvider.createAccessToken(userEmail);
        Integer exprTime = 1800000;
//        Integer exprTime = 5000;
        String refreshToken = tokenProvider.createRefreshToken(userEmail);
        Integer refreshExprTime = 604800000;


        SignInResponseDto signInResponseDto = new SignInResponseDto(token, exprTime, refreshToken, refreshExprTime, userEntity);
        return ResponseDto.setSuccess("Success", signInResponseDto);
    }

    public ResponseDto<Void> logout(String token) {
        Long expiration = tokenProvider.getExpiration(token);
        redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS);
        redisTemplate.opsForSet().add("Blacklist", token);

        return ResponseDto.setSuccess();

    }

    public ResponseDto<RefreshResponseDto> getAccess(String refreshToken) {
        String accessToken = tokenProvider.createAccessTokenFromRefreshToken(refreshToken);
        Integer exprTime = 1800000;
//            Integer exprTime = 5000;

        RefreshResponseDto refreshResponseDto = new RefreshResponseDto(accessToken, exprTime);

        return ResponseDto.setSuccess("Success", refreshResponseDto);
    }


}
