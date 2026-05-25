package atmin;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GenieException extends RuntimeException {
    private final HttpStatus status;
    public GenieException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}