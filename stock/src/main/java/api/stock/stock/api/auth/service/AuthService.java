package api.stock.stock.api.auth.service;

import api.stock.stock.api.auth.dto.request.SignInDto;
import api.stock.stock.api.auth.dto.request.SignUpDto;
import api.stock.stock.api.auth.dto.response.RefreshResponseDto;
import api.stock.stock.api.auth.dto.response.SignInResponseDto;
import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
import api.stock.stock.api.user.domain.dto.UserDto;
import api.stock.stock.api.user.domain.entity.UserEntity;
import api.stock.stock.api.user.repository.UserRepository;
import api.stock.stock.global.security.TokenProvider;
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

    public UserDto signUp(SignUpDto dto) {
        UserEntity userEntity = modelMapper.map(dto, UserEntity.class);
        String encodedPassword = passwordEncoder.encode(dto.getUserPassword());
        userEntity.setUserPassword(encodedPassword);
        userRepository.save(userEntity);
        return UserDto.from(userEntity);
    }

    public void emailCheck(String userEmail) {
        if (userRepository.existsById(userEmail)) {
            throw new IPOApplicationException(ErrorCode.DUPLICATED_USER_EMAIL);
        }
    }

    public void nicknameCheck(String userNickname) {
        if (userRepository.existsByUserNickname(userNickname)) {
            throw new IPOApplicationException(ErrorCode.DUPLICATED_USER_NICKNAME);
        }
    }


    @Transactional(readOnly = true)
    public SignInResponseDto signIn(SignInDto dto) {
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


        return SignInResponseDto.of(token, exprTime, refreshToken, refreshExprTime, UserDto.from(userEntity));
    }

    public void logout(String token) {
        Long expiration = tokenProvider.getExpiration(token);
        redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS);
        redisTemplate.opsForSet().add("Blacklist", token);
    }

    public RefreshResponseDto getAccess(String refreshToken) {
        String accessToken = tokenProvider.createAccessTokenFromRefreshToken(refreshToken);
        Integer exprTime = 1800000;
//            Integer exprTime = 5000;

        return RefreshResponseDto.of(accessToken, exprTime);
    }


}
