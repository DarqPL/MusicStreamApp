package iuh.fit.MusicStreamAppBackEnd.dto;

import lombok.Data;
import java.util.List;

@Data
public class SearchResultDTO {
    private List<TrackDTO> tracks;
    private List<UserDTO> creators; // (Artist/User)
    private List<AlbumDTO> albums;
}