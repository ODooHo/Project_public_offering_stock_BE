package api.stock.stock.api.user.service;

import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
import api.stock.stock.api.file.FileService;
import api.stock.stock.api.user.domain.dto.PatchUserResponseDto;
import api.stock.stock.api.user.domain.entity.UserEntity;
import api.stock.stock.api.user.repository.UserRepository;
import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final FileService fileService;


    public UserService(UserRepository userRepository, FileService fileService) {
        this.userRepository = userRepository;
        this.fileService = fileService;
    }


    public ResponseDto<PatchUserResponseDto> patchUser(String userNickname, MultipartFile userProfile, String userEmail) {
        UserEntity userEntity = null;
        userEntity = userRepository.findById(userEmail).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userEmail is %s", userEmail))
        );
        if (userNickname != null) {
            userEntity.setUserNickname(userNickname);
        }
        fileService.setProfile(userProfile, userEmail);
        userRepository.save(userEntity);
        userEntity.setUserPassword("");
        PatchUserResponseDto patchUserResponseDto = new PatchUserResponseDto(userEntity);

        return ResponseDto.setSuccess("Success", patchUserResponseDto);
    }

    public void withDraw(String userEmail) {
        userRepository.deleteById(userEmail);
    }

}
