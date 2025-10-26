package iuh.fit.MusicStreamAppBackEnd.dto;

import lombok.Data;

@Data
public class UserProfileUpdateDTO {
    // Cho phép cập nhật từng phần, không cần @NotBlank
    private String profilePictureUrl;
    private String bio;
}