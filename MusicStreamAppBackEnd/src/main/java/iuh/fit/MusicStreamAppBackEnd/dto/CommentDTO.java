package iuh.fit.MusicStreamAppBackEnd.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentDTO {
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private UserDTO user;
    private Long parentCommentId; // Để frontend biết đây là comment trả lời
    private List<CommentDTO> replies; // Danh sách các comment trả lời (nếu có)
}