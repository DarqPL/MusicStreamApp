package iuh.fit.MusicStreamAppBackEnd.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String profilePictureUrl;
    private boolean isVerified;
}