package iuh.fit.MusicStreamAppBackEnd.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlaylistTrackRequestDTO {
    @NotNull(message = "trackId không được để trống")
    private Long trackId;
}