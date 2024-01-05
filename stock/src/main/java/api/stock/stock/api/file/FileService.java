package api.stock.stock.api.file;

import api.stock.stock.api.community.board.BoardEntity;
import api.stock.stock.api.community.board.BoardService;

import api.stock.stock.api.user.UserService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@Service
@Slf4j
public class FileService {

    private final UserService userService;
    private final BoardService boardService;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public FileService(UserService userService, BoardService boardService, AmazonS3 amazonS3) {
        this.userService = userService;
        this.boardService = boardService;
        this.amazonS3 = amazonS3;
    }

    public ResponseDto<String> uploadImage(MultipartFile boardImage, BoardEntity board) {
        String imageName;
        Integer boardId = board.getBoardId();
        try{
            if (boardImage != null){
                imageName = boardId + ".jpg";
                boardService.setImageName(boardId,imageName);;
                String path = uploadDir + "img/" + imageName;
                uploadFileToS3(boardImage,path);
            }else{
                boardService.setImageName(boardId, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("DataBase Error!");
        }

        return ResponseDto.setSuccess("Success", "OK");
    }

    public ResponseDto<String> setProfile(MultipartFile file, String userEmail) {

        String imageName = userEmail + ".jpg";
        try {
            // S3 버킷에 파일 업로드
            uploadFileToS3(file, uploadDir + "profile/"+imageName);
            userService.setProfile(userEmail,imageName);
            boardService.updateProfile(userEmail,imageName);

            return ResponseDto.setSuccess("Success", imageName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("Database or S3 Error");
        }
    }

    public ResponseEntity<byte[]> getProfileImage(String userEmail){
        String imageName = userEmail + ".jpg";

        return getImage(imageName,"profile/");
    }

    public ResponseEntity<byte[]> getBoardImage(Integer boardId){
        String imageName = boardId + ".jpg";
        return getImage(imageName,"img/");
    }

    public ResponseDto<String> deleteBoardImage(Integer boardId){
        String imageName = boardId + ".jpg";
        String path = uploadDir + "img/" + imageName;
        try{
            amazonS3.deleteObject(bucketName,path);
        }catch (AmazonS3Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed("S3 Error");
        }
        return ResponseDto.setSuccess("Success","Delete Completed");

    }

    public ResponseDto<String> deleteProfileImage(String userEmail){
        String imageName = userEmail + ".jpg";
        String path = uploadDir + "profile/" + imageName;
        try{
            amazonS3.deleteObject(bucketName,path);
        }catch (AmazonS3Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed("S3 Error");
        }
        return ResponseDto.setSuccess("Success","Delete Completed");

    }




    private ResponseEntity<byte[]> getImage(String imageName, String path) {
        if (imageName == null){
            HttpHeaders headers = new HttpHeaders();
            MediaType mediaType = MediaType.ALL;
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(null);
        }
        try {
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
        } catch (AmazonS3Exception e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    private void uploadFileToS3(MultipartFile file, String s3Key) {
        try {
            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(new PutObjectRequest(bucketName, s3Key, inputStream, metadata));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private String getExtension(String fileDirectory, String fileId){
//        File folder = new File(fileDirectory);
//
//        FilenameFilter filter = (dir, name) -> {
//            try{
//                return name.startsWith(fileId);
//            }catch(Exception e){
//                e.printStackTrace();
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
