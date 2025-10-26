package iuh.fit.MusicStreamAppBackEnd.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequestDTO {
    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    private Long parentCommentId; // Sẽ là null nếu đây là bình luận gốc
}