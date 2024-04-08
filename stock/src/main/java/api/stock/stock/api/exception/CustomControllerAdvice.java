package api.stock.stock.api.exception;

import api.stock.stock.global.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(IPOApplicationException.class)
    public ResponseEntity<?> errorHandler(IPOApplicationException e){
        log.error("Error occurs {}",e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(ResponseDto.error(e.getErrorCode().name()));
    }
}
