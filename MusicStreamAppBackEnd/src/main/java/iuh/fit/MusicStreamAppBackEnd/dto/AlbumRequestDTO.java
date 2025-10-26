package iuh.fit.MusicStreamAppBackEnd.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AlbumRequestDTO {
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Ảnh bìa không được để trống")
    private String coverArtUrl; // Giả sử đã tải lên Cloudinary
}