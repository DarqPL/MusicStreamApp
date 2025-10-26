package iuh.fit.MusicStreamAppBackEnd.exception;

import iuh.fit.MusicStreamAppBackEnd.dto.ErrorDetailsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice // Annotation này cho phép lớp này bắt exception từ mọi Controller
public class GlobalExceptionHandler {

    /**
     * Bắt lỗi ResourceNotFoundException (404)
     * Kích hoạt khi findById().orElseThrow(...)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetailsDTO> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Bắt lỗi DuplicateResourceException (409)
     * Kích hoạt khi đăng ký trùng username/email.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorDetailsDTO> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    /**
     * Bắt lỗi UnauthorizedException (403)
     * Kích hoạt khi user không phải chủ sở hữu (ví dụ: sửa playlist của người khác).
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDetailsDTO> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    /**
     * Bắt lỗi AccessDeniedException (403)
     * Kích hoạt bởi @PreAuthorize khi user không có quyền.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetailsDTO> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "Bạn không có quyền truy cập tài nguyên này.",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    /**
     * Bắt lỗi Validation (400)
     * Kích hoạt khi DTO dính annotation @Valid thất bại (ví dụ: @NotBlank).
     * Trả về một Map các lỗi.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("errors", errors); // Đối tượng chứa các lỗi cụ thể
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Bắt tất cả các lỗi 500 (Internal Server Error)
     * Đây là "chốt chặn" cuối cùng, trả về một thông báo chung chung.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailsDTO> handleGlobalException(Exception ex, WebRequest request) {
        // In lỗi chi tiết ra console để dev debug
        ex.printStackTrace();

        ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Đã có lỗi xảy ra ở phía máy chủ, vui lòng thử lại sau.",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}