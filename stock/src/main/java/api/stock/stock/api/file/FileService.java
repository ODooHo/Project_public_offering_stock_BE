package api.stock.stock.api.file;

import api.stock.stock.api.community.board.BoardEntity;
import api.stock.stock.api.community.board.BoardRepository;
import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
import api.stock.stock.api.user.UserEntity;
import api.stock.stock.api.user.UserRepository;
import api.stock.stock.global.response.ResponseDto;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Service
@Slf4j
public class FileService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${file.upload-dir}")
    private String uploadDir;


    @Autowired
    public FileService(UserRepository userRepository, BoardRepository boardRepository, AmazonS3 amazonS3) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;

        this.amazonS3 = amazonS3;
    }

    /**
     * entity 주고받는거 dto로 변환할 생각 해보자.
     * 근데 아마 이게 반영이 제대로 안되는거로 알고있음
     */

    @Transactional
    public ResponseDto<Void> uploadImage(MultipartFile boardImage, BoardEntity board) {
        if (boardImage != null) {
            String fileName = board.getBoardId() + ".jpg";
            board.setBoardImage(fileName);
            String path = uploadDir + "img/" + fileName;
            uploadFileToS3(boardImage, path);
        } else {
            board.setBoardImage(null);
        }
        boardRepository.save(board);
        return ResponseDto.setSuccess();
    }

    @Transactional
    public ResponseDto<Void> setProfile(MultipartFile file, String userEmail) {
        UserEntity user = userRepository.findById(userEmail).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.USER_NOT_FOUND, String.format("user email is %s", userEmail))
        );
        List<BoardEntity> boardList = boardRepository.findByBoardWriterEmail(userEmail);
        String fileName = user.getUserEmail() + "." + "jpg";
        // S3 버킷에 파일 업로드
        uploadFileToS3(file, uploadDir + "profile/" + fileName);
        user.setUserProfile(fileName);
        userRepository.save(user);
        for (BoardEntity board : boardList) {
            board.setBoardWriterProfile(fileName);
            boardRepository.save(board);
        }
        return ResponseDto.setSuccess();
    }

    public ResponseEntity<byte[]> getProfileImage(String userEmail) {
        String imageName = userEmail + ".jpg";
        return getImage(imageName, "profile/");
    }

    public ResponseEntity<byte[]> getBoardImage(Integer boardId) {
        String imageName = boardId + ".jpg";
        return getImage(imageName, "img/");
    }

    public void deleteBoardImage(Integer boardId) {
        String imageName = boardId + ".jpg";
        String path = uploadDir + "img/" + imageName;
        amazonS3.deleteObject(bucketName, path);
    }

    public ResponseDto<Void> deleteProfileImage(String userEmail) {
        String imageName = userEmail + ".jpg";
        String path = uploadDir + "profile/" + imageName;
        amazonS3.deleteObject(bucketName, path);
        return ResponseDto.setSuccess();
    }


    private ResponseEntity<byte[]> getImage(String imageName, String path) {
        if (imageName == null) {
            HttpHeaders headers = new HttpHeaders();
            MediaType mediaType = MediaType.ALL;
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(null);
        }
        try{
            String fileName = imageName + ".jpg";

            S3Object s3Object = amazonS3.getObject(bucketName, uploadDir + path + fileName);
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
            byte[] imageData = IOUtils.toByteArray(objectInputStream);

            HttpHeaders headers = new HttpHeaders();
            MediaType mediaType = MediaType.IMAGE_JPEG;

            headers.setContentType(mediaType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private void uploadFileToS3(MultipartFile file, String s3Key) {
        try {
            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(new PutObjectRequest(bucketName, s3Key, inputStream, metadata));
        } catch (IOException e) {
            throw new IPOApplicationException(ErrorCode.DATABASE_ERROR,"IOException");
        }
    }

//    private String getExtension(String fileDirectory, String fileId){
//        File folder = new File(fileDirectory);
//
//        FilenameFilter filter = (dir, name) -> {
//            try{
//                return name.startsWith(fileId);
//            }catch(Exception e){
//                throw new RuntimeException(e);
//                return false;
//            }
//        };
//
//        String[] files = folder.list(filter);
//
//        if (files == null || files.length == 0) {
//            return "";
//        }
//
//        String fileName = files[0];
//
//        // 확장자 추출
//        int dotIndex = fileName.lastIndexOf('.');
//        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
//            return fileName.substring(dotIndex);
//        }
//        //파일이 없다면
//
//        return "";
//    }
//
//    private String setFileName(MultipartFile file, BoardEntity board) {
//        String originalFileName = file.getOriginalFilename();
//        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
//        String fileName = board.getBoardId() + "." + extension;
//        return fileName;
//    }

}
