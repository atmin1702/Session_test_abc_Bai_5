package atmin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/genie")
public class GenieController {

    private static final int MAX_WISHES = 3;
    private int wishCount = 0;
    private final List<WishLog> history = new ArrayList<>();

    // Middleware kiểm tra lượt ước
    private void checkWishLimit(String type, String detail) {
        if (wishCount >= MAX_WISHES) {
            history.add(new WishLog(type, detail, "REJECTED", "Hết lượt ước", LocalDateTime.now()));
            throw new GenieException("Thần đèn đã hết lượt ước! Hãy đợi đến kiếp sau.", HttpStatus.FORBIDDEN);
        }
    }

    // Điều ước 1: Tạo vật phẩm
    @PostMapping("/wish/create-item")
    public ResponseEntity<String> createItem(@RequestBody WishRequest request) {
        if (request.getDetail() == null || request.getDetail().isEmpty()) {
            throw new GenieException("Bạn phải nói rõ muốn tạo gì!", HttpStatus.BAD_REQUEST);
        }

        checkWishLimit("CREATE_ITEM", request.getDetail());

        wishCount++;
        history.add(new WishLog("CREATE_ITEM", request.getDetail(), "SUCCESS", "Đã hiện thực hóa", LocalDateTime.now()));
        return ResponseEntity.status(HttpStatus.CREATED).body("Bùm! " + request.getDetail() + " của bạn đã xuất hiện!");
    }

    // Điều ước 2: Thay đổi trạng thái
    @PutMapping("/wish/update-status")
    public ResponseEntity<String> updateStatus(@RequestBody WishRequest request) {
        checkWishLimit("UPDATE_STATUS", request.getDetail());

        if (request.getDetail().contains("giàu")) {
            history.add(new WishLog("UPDATE_STATUS", request.getDetail(), "REJECTED", "Ước sai quy tắc", LocalDateTime.now()));
            throw new GenieException("Ước giàu có là không hợp lệ, Thần đèn chỉ có thể giúp bạn tìm việc tốt hơn!", HttpStatus.BAD_REQUEST);
        }

        wishCount++;
        history.add(new WishLog("UPDATE_STATUS", request.getDetail(), "SUCCESS", "Đã đổi vận", LocalDateTime.now()));
        return ResponseEntity.ok("Vận mệnh của bạn đã thay đổi: " + request.getDetail());
    }

    // Điều ước 3: Lời khuyên ngẫu nhiên
    @GetMapping("/wish/random-wisdom")
    public ResponseEntity<String> getRandomWisdom() {
        checkWishLimit("GET_WISDOM", "Xin lời khuyên");

        String[] wisdoms = {"Hãy code sạch bóng warning", "Học Spring MVC thuần trước khi dùng Boot", "Đừng bao giờ quên semicolon"};
        String result = wisdoms[new Random().nextInt(wisdoms.length)];

        wishCount++;
        history.add(new WishLog("GET_WISDOM", "Xin lời khuyên", "SUCCESS", result, LocalDateTime.now()));
        return ResponseEntity.ok("Thần đèn phán: " + result);
    }

    // Xem lịch sử
    @GetMapping("/history")
    public List<WishLog> getHistory() {
        return history;
    }
}