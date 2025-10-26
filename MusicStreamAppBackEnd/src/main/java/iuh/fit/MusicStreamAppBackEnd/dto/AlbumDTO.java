package iuh.fit.MusicStreamAppBackEnd.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class AlbumDTO {
    private Long albumId;
    private String title;
    private String coverArtUrl;
    private LocalDate releaseDate;
    private UserDTO creator;
    private Set<TrackDTO> tracks; // Danh sách các bài hát trong album
}