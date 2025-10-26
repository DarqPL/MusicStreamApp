package iuh.fit.MusicStreamAppBackEnd.dto;

import iuh.fit.MusicStreamAppBackEnd.entity.Post;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostRequestDTO {
    @NotNull
    private Post.PostType postType;

    // Chỉ 1 trong 2 trường này được có giá trị
    private Long trackId; // Dùng cho NEW_TRACK
    private Long originalPostId; // Dùng cho REPOST
}