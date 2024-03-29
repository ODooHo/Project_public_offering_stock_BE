package api.stock.stock.api.user;

import api.stock.stock.api.file.FileService;
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

        try{
            userEntity = userRepository.findById(userEmail).orElse(null);
            if(userEntity == null){
                return ResponseDto.setFailed("Does Not Exist User");
            }
            if (userNickname != null) {
                userEntity.setUserNickname(userNickname);
            }
            fileService.setProfile(userProfile, userEmail);
            userRepository.save(userEntity);


        }catch (Exception e){
            throw new RuntimeException(e);
             
        }

        userEntity.setUserPassword("");
        PatchUserResponseDto patchUserResponseDto = new PatchUserResponseDto(userEntity);

        return ResponseDto.setSuccess("Success",patchUserResponseDto);
    }

    public void withDraw(String userEmail){
        try{
            userRepository.deleteById(userEmail);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void setProfile(String userEmail, String profileName){
        UserEntity user = null;
        try{
            user = userRepository.findById(userEmail).orElse(null);
            user.setUserProfile(profileName);
            userRepository.save(user);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void deleteProfile(String userEmail){
        UserEntity user = null;
        try{
            user = userRepository.findById(userEmail).orElse(null);
            user.setUserProfile("default.jpg");
            userRepository.save(user);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
