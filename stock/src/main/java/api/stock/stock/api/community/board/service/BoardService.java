package api.stock.stock.api.community.board.service;

import api.stock.stock.api.community.board.domain.dto.BoardDto;
import api.stock.stock.api.community.board.domain.dto.request.PatchBoardRequestDto;
import api.stock.stock.api.community.board.domain.entity.BoardEntity;
import api.stock.stock.api.community.board.repository.BoardRepository;
import api.stock.stock.api.exception.ErrorCode;
import api.stock.stock.api.exception.IPOApplicationException;
import api.stock.stock.api.file.FileService;
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


    public BoardDto register(
            String boardTitle,
            String boardContent,
            String boardWriterEmail,
            String boardWriterProfile,
            String boardWriterNickname,
            MultipartFile boardImage) {
        BoardEntity board = BoardEntity.of(boardTitle, boardContent, boardWriterEmail, boardWriterProfile, boardWriterNickname, LocalDate.now());
        boardRepository.save(board);
        fileService.uploadImage(boardImage, board);
        boardRepository.save(board);
        return BoardDto.from(board);
    }

    public BoardDto getBoard(Integer boardId) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        int current = board.getBoardClickCount();
        board.setBoardClickCount(current + 1);

        return BoardDto.from(board);
    }

    @Transactional(readOnly = true)
    public List<BoardDto> getList() {
        return (boardRepository.findList().stream().map(BoardDto::from).toList());
    }

    @Transactional(readOnly = true)
    public Integer getLikeCount(Integer boardId) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        return board.getBoardLikeCount();
    }


    public BoardDto patchBoard(String userEmail, Integer boardId, PatchBoardRequestDto dto) {
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

        return BoardDto.from(board);
    }

    public void deleteBoard(String userEmail, Integer boardId) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        String boardWriterEmail = board.getBoardWriterEmail();
        if (!userEmail.equals(boardWriterEmail)) {
            throw new IPOApplicationException(ErrorCode.INVALID_PERMISSION);
        }
        boardRepository.deleteById(boardId);
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
    public List<BoardDto> searchBoard(String searchWord) {
        return boardRepository.findByBoardTitleContains(searchWord).stream().map(BoardDto::from).toList();
    }

    public void increaseComment(Integer boardId) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        int count = board.getBoardCommentCount() + 1;
        board.setBoardCommentCount(count);
    }

    public void updateProfile(String userEmail, String profileName) {
        List<BoardEntity> boardList = boardRepository.findByBoardWriterEmail(userEmail);
        for (BoardEntity board : boardList) {
            board.setBoardWriterProfile(profileName);
        }
    }


    public void updateLike(Integer boardId, Integer count) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        board.setBoardLikeCount(count);
    }

    public void increaseLike(Integer boardId) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        int count = board.getBoardLikeCount() + 1;
        board.setBoardLikeCount(count);
    }

    public void decreaseLike(Integer boardId) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        int count = board.getBoardLikeCount() - 1;
        board.setBoardLikeCount(count);
    }

    public void setImageName(Integer boardId, String imageName) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(
                () -> new IPOApplicationException(ErrorCode.BOARD_NOT_FOUND)
        );
        board.setBoardImage(imageName);
    }


}
