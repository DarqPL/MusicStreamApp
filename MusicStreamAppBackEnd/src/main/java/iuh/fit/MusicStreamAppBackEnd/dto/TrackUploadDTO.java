package iuh.fit.MusicStreamAppBackEnd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrackUploadDTO {
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotNull(message = "Thời lượng không được để trống")
    private int duration;

    @NotBlank(message = "Đường dẫn file không được để trống")
    private String audioFileUrl; // Frontend sẽ tải file lên Cloudinary trước, sau đó gửi URL về đây

    private Long albumId; // Có thể không thuộc album nào
}