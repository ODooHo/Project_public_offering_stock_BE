package api.stock.stock.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class IPOApplicationException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;

    public IPOApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }

    @Override
    public String getMessage() {
        if(message == null){
            return errorCode.getMessage();
        }else{
            return String.format("%s. %s", errorCode.getMessage(),message);
        }
    }
}
