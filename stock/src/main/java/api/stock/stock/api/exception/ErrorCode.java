package api.stock.stock.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"Invalid token"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"User not founded"),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND,"Board not founded"),
    TRADE_NOT_FOUND(HttpStatus.NOT_FOUND,"Trade not founded"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"Comment not founded"),
    IPO_NOT_FOUND(HttpStatus.NOT_FOUND,"Ipo not founded"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"Invalid password"),
    INVALID_PASSWORD_CHECK(HttpStatus.UNAUTHORIZED,"Invalid passwordCheck"),
    DUPLICATED_USER_EMAIL(HttpStatus.CONFLICT,"Duplicated user email"),
    DUPLICATED_USER_NICKNAME(HttpStatus.CONFLICT,"Duplicated user nickname" ),
    DUPLICATED_LIKED(HttpStatus.CONFLICT,"Already liked post"),
    DUPLICATED_FAVOR(HttpStatus.CONFLICT,"Already Favored IPO"),
    DUPLICATED_TRADE_NAME(HttpStatus.CONFLICT,"Already registered trade"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED,"Invalid permission"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Database error occurred")
    ;

    private final HttpStatus status;
    private final String message;
}
