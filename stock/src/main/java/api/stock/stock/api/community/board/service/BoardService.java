package api.stock.stock.api.community.board.service;

import api.stock.stock.api.community.board.domain.entity.BoardEntity;
import api.stock.stock.api.community.board.domain.dto.PatchBoardDto;
import api.stock.stock.api.community.board.domain.dto.PatchBoardResponseDto;
import api.stock.stock.api.community.board.repository.BoardRepository;
import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
import api.stock.stock.api.file.FileService;
import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@Slf4j
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
        fileService.uploadImage(boardImage, board);
        boardRepository.save(board);
        return ResponseDto.setSuccess("Success", board);
    }

    @Transactional(readOnly = true)
    public ResponseDto<BoardEntity> getBoard(Integer boardId) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        int current = board.getBoardClickCount();
        board.setBoardClickCount(current + 1);
        boardRepository.save(board);
        return ResponseDto.setSuccess("Success", board);
    }

    @Transactional(readOnly = true)
    public ResponseDto<List<BoardEntity>> getList() {
        return ResponseDto.setSuccess("Success", boardRepository.findList());
    }

    @Transactional(readOnly = true)
    public Integer getLikeCount(Integer boardId) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        return board.getBoardLikeCount();
    }


    public ResponseDto<PatchBoardResponseDto> patchBoard(String userEmail, Integer boardId, PatchBoardDto dto) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        String boardUserEmail = board.getBoardWriterEmail();
        if (!userEmail.equals(boardUserEmail)) {
            throw new IPOApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        board.setBoardTitle(dto.getBoardTitle());
        board.setBoardContent(dto.getBoardContent());
        board.setBoardWriteDate(LocalDate.now());
        boardRepository.save(board);

        PatchBoardResponseDto response = new PatchBoardResponseDto(board);


        return ResponseDto.setSuccess("Success", response);
    }

    public ResponseDto<String> deleteBoard(String userEmail, Integer boardId) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        String boardWriterEmail = board.getBoardWriterEmail();
        if (!userEmail.equals(boardWriterEmail)) {
            throw new IPOApplicationException(ErrorCode.INVALID_PERMISSION);
        }
        boardRepository.deleteById(boardId);
        return ResponseDto.setSuccess("Success", "Delete Completed");
    }

    public void deleteByWithdraw(String userEmail) {
        List<BoardEntity> boardList = boardRepository.findByBoardWriterEmail(userEmail);
        for (BoardEntity board : boardList) {
            Integer boardId = board.getBoardId();
            fileService.deleteBoardImage(boardId);
            deleteBoard(userEmail, boardId);
        }
    }

    @Transactional(readOnly = true)
    public List<BoardEntity> searchBoard(String searchWord) {
        return boardRepository.findByBoardTitleContains(searchWord);
    }

    public void increaseComment(Integer boardId) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        Integer count = board.getBoardCommentCount() + 1;
        board.setBoardCommentCount(count);
        boardRepository.save(board);
    }

    public void updateProfile(String userEmail, String profileName) {
        List<BoardEntity> boardList = boardRepository.findByBoardWriterEmail(userEmail);
        for (BoardEntity board : boardList) {
            board.setBoardWriterProfile(profileName);
            boardRepository.save(board);
        }
    }


    public void updateLike(Integer boardId, Integer count) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        board.setBoardLikeCount(count);
        boardRepository.save(board);
    }

    public void increaseLike(Integer boardId) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        Integer count = board.getBoardLikeCount() + 1;
        board.setBoardLikeCount(count);
        boardRepository.save(board);
    }

    public void decreaseLike(Integer boardId) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        Integer count = board.getBoardLikeCount() - 1;
        board.setBoardLikeCount(count);
        boardRepository.save(board);
    }

    public void setImageName(Integer boardId, String imageName) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        board.setBoardImage(imageName);
        boardRepository.save(board);

    }


}
