package api.stock.stock.api.community.board;

import api.stock.stock.api.file.FileService;
import api.stock.stock.global.response.ResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final FileService fileService;


    public BoardService(BoardRepository boardRepository, FileService fileService) {
        this.boardRepository = boardRepository;

        this.fileService = fileService;
    }



    public ResponseDto<BoardEntity> register(
            String boardTitle,
            String boardContent,
            String boardWriterEmail,
            String boardWriterProfile,
            String boardWriterNickname,
            MultipartFile boardImage) {


        BoardEntity board = new BoardEntity();
        board.setBoardTitle(boardTitle);
        board.setBoardContent(boardContent);
        board.setBoardWriterEmail(boardWriterEmail);
        board.setBoardWriterProfile(boardWriterProfile);
        board.setBoardWriterNickname(boardWriterNickname);
        board.setBoardWriteDate(LocalDate.now());
        boardRepository.save(board);
        try{
            fileService.uploadImage(boardImage,board);
            boardRepository.save(board);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed("DataBase Error");
        }
        return ResponseDto.setSuccess("Success",board);
    }

    public ResponseDto<BoardEntity> getBoard(Integer boardId) {
        BoardEntity board = new BoardEntity();
        try{
            board = boardRepository.findById(boardId).orElse(null);
            Integer current = board.getBoardClickCount();
            board.setBoardClickCount(current + 1);
            boardRepository.save(board);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("DataBase Error");
        }
        return ResponseDto.setSuccess("Success",board);
    }

    public ResponseDto<List<BoardEntity>> getList() {
        List<BoardEntity> boardList = new ArrayList<>();
        try{
            boardList = boardRepository.findList();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed("DataBase Error");
        }

        return ResponseDto.setSuccess("Success", boardList);
    }

    public Integer getLikeCount(Integer boardId){
        BoardEntity board = null;
        Integer result = null;
        try{
            board = boardRepository.findById(boardId).orElse(null);
            result = board.getBoardLikeCount();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return result;
    }


    public ResponseDto<PatchBoardResponseDto> patchBoard(String userEmail, Integer boardId, PatchBoardDto dto){
        BoardEntity board = boardRepository.findById(boardId).orElse(null);
        String boardUserEmail = board.getBoardWriterEmail();
        if(!userEmail.equals(boardUserEmail)){
            return ResponseDto.setFailed("Wrong Request(userEmail doesn't Match)");
        }
        String boardTitle = dto.getBoardTitle();
        String boardContent = dto.getBoardContent();
        LocalDate date = LocalDate.now();

        try{
            board.setBoardTitle(boardTitle);
            board.setBoardContent(boardContent);
            board.setBoardWriteDate(date);
            boardRepository.save(board);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed("DataBase Error");
        }
        PatchBoardResponseDto response = new PatchBoardResponseDto(board);


        return ResponseDto.setSuccess("Success",response);
    }

    public ResponseDto<String> deleteBoard(String userEmail, Integer boardId){
        BoardEntity board = boardRepository.findById(boardId).orElse(null);
        String boardWriterEmail = board.getBoardWriterEmail();
        if(!userEmail.equals(boardWriterEmail)){
            return ResponseDto.setFailed("Wrong Request(userEmail doesn't Match)");
        }
        try{
            boardRepository.deleteById(boardId);
        }catch (Exception e){
            return ResponseDto.setFailed("DataBase Error");
        }
        return ResponseDto.setSuccess("Success","Delete Completed");
    }

    public ResponseDto<String> deleteByWithdraw(String userEmail){
        List<BoardEntity> boardList = boardRepository.findByBoardWriterEmail(userEmail);

        try{
            for (BoardEntity board : boardList) {
                Integer boardId = board.getBoardId();
                fileService.deleteBoardImage(boardId);
                deleteBoard(userEmail,boardId);
            }
        }catch (Exception e){
            return ResponseDto.setFailed("DataBase Error");
        }
        return ResponseDto.setSuccess("Success" , "Delete Completed");
    }

    public List<BoardEntity> searchBoard(String searchWord){
        List<BoardEntity> result = new ArrayList<>();
        try{
            result = boardRepository.findByBoardTitleContains(searchWord);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public void increaseComment(Integer boardId){
        BoardEntity board = null;
        try{
            board = boardRepository.findById(boardId).orElse(null);
            Integer count = board.getBoardCommentCount() + 1;
            board.setBoardCommentCount(count);
            boardRepository.save(board);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateProfile(String userEmail, String profileName){
        List<BoardEntity> boardList = boardRepository.findByBoardWriterEmail(userEmail);
        try{
            for (BoardEntity board : boardList) {
                board.setBoardWriterProfile(profileName);
                boardRepository.save(board);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void updateLike(Integer boardId,Integer count){
        BoardEntity board = null;
        try{
            board = boardRepository.findById(boardId).orElse(null);
            board.setBoardLikeCount(count);
            boardRepository.save(board);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void increaseLike(Integer boardId){
        BoardEntity board = null;
        try{
            board = boardRepository.findById(boardId).orElse(null);
            Integer count = board.getBoardLikeCount() + 1;
            board.setBoardLikeCount(count);
            boardRepository.save(board);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void decreaseLike(Integer boardId){
        BoardEntity board = null;
        try{
            board = boardRepository.findById(boardId).orElse(null);
            Integer count = board.getBoardLikeCount() - 1;
            board.setBoardLikeCount(count);
            boardRepository.save(board);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setImageName(Integer boardId, String imageName) {
        BoardEntity board = null;
        try{
            board = boardRepository.findById(boardId).orElse(null);
            board.setBoardImage(imageName);
            boardRepository.save(board);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
