package iuh.fit.MusicStreamAppBackEnd.dto;

import iuh.fit.MusicStreamAppBackEnd.entity.Post;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostDTO {
    private Long postId;
    private Post.PostType postType;
    private LocalDateTime createdAt;
    private UserDTO user; // Người đăng

    // Nội dung bài đăng
    private TrackDTO track; // Nếu là NEW_TRACK
    private PostDTO originalPost; // Nếu là REPOST

    // Thống kê tương tác
    private int likesCount;
    private int commentsCount;
    private int repostsCount;
    private boolean isLikedByCurrentUser; // Rất quan trọng cho UI
}