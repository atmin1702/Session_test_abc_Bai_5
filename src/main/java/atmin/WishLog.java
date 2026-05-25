package atmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishLog {
    private String wishType;
    private String content;
    private String status; // SUCCESS or REJECTED
    private String message;
    private LocalDateTime timestamp;
}