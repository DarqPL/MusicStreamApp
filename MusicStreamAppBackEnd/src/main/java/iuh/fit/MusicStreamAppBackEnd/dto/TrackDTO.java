package iuh.fit.MusicStreamAppBackEnd.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TrackDTO {
    private Long trackId;
    private String title;
    private int duration;
    private int playCount;
    private String audioFileUrl;
    private LocalDate releaseDate;
    private UserDTO creator;
    private AlbumDTO album; // Album có thể null
}