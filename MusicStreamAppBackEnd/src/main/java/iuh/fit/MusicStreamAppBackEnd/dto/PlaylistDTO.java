package iuh.fit.MusicStreamAppBackEnd.dto;

import lombok.Data;
import java.util.List;

@Data
public class PlaylistDTO {
    private Long playlistId;
    private String name;
    private UserDTO user; // Người tạo playlist
    private List<TrackDTO> tracks; // Danh sách bài hát đã được sắp xếp
}