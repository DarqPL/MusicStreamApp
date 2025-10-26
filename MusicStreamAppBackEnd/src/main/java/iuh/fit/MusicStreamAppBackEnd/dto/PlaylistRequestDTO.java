package iuh.fit.MusicStreamAppBackEnd.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlaylistRequestDTO {
    @NotBlank(message = "Tên playlist không được để trống")
    private String name;
}