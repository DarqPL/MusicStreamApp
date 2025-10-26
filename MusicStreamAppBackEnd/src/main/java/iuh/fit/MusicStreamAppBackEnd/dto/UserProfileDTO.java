package iuh.fit.MusicStreamAppBackEnd.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserProfileDTO {
    private Long userId;
    private String username;
    private String email; // Chỉ hiển thị cho chính chủ
    private String profilePictureUrl;
    private String bio;
    private boolean isVerified;
    private LocalDateTime createdAt;

    // Thống kê xã hội
    private int followersCount;
    private int followingCount;
    private int trackCount;
    private int likedTracksCount;
}