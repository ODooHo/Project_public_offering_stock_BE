package api.stock.stock.global.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    String message;
    T data;


    public static <T> ResponseDto<T> setSuccess() {
        return new ResponseDto<T>("Success", null);
    }

    public static <T> ResponseDto<T> setSuccess(String message, T data) {
        return new ResponseDto<>("Success", data);
    }

    public static ResponseDto<Void> error(String message) {
        return new ResponseDto<Void>(message, null);
    }

    public String toStream() {
        if (data == null) {
            return "{" +
                    "\"message\":" + "\"" + message + "\"," +
                    "\"data\":" + null +
                    "}";
        }
        return "{" +
                "\"message\":" + "\"" + message + "\"," +
                "\"data\":" + "\"" + data + "\"," +
                "}";
    }
}
