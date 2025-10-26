package iuh.fit.MusicStreamAppBackEnd.controller;

import iuh.fit.MusicStreamAppBackEnd.dto.PlaylistDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.PlaylistRequestDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.PlaylistTrackRequestDTO;
import iuh.fit.MusicStreamAppBackEnd.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    /**
     * Lấy thông tin chi tiết một playlist.
     * Công khai.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDTO> getPlaylistById(@PathVariable Long id) {
        PlaylistDTO playlist = playlistService.getPlaylistById(id);
        return ResponseEntity.ok(playlist);
    }

    /**
     * Lấy danh sách playlist của một user bất kỳ.
     * Công khai.
     */
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<PlaylistDTO>> getPlaylistsByUserId(@PathVariable Long userId) {
        List<PlaylistDTO> playlists = playlistService.getPlaylistsByUser(userId);
        return ResponseEntity.ok(playlists);
    }

    /**
     * Tạo một playlist mới.
     * Yêu cầu đã đăng nhập.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlaylistDTO> createPlaylist(@Valid @RequestBody PlaylistRequestDTO request) {
        PlaylistDTO newPlaylist = playlistService.createPlaylist(request);
        return new ResponseEntity<>(newPlaylist, HttpStatus.CREATED);
    }

    /**
     * Đổi tên một playlist.
     * Yêu cầu là chủ sở hữu.
     */
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlaylistDTO> updatePlaylist(@PathVariable Long id, @Valid @RequestBody PlaylistRequestDTO request) {
        PlaylistDTO updatedPlaylist = playlistService.updatePlaylistName(id, request);
        return ResponseEntity.ok(updatedPlaylist);
    }

    /**
     * Xóa một playlist.
     * Yêu cầu là chủ sở hữu.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Thêm một bài hát vào playlist.
     * Yêu cầu là chủ sở hữu.
     */
    @PostMapping("/{playlistId}/tracks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlaylistDTO> addTrackToPlaylist(@PathVariable Long playlistId, @Valid @RequestBody PlaylistTrackRequestDTO request) {
        PlaylistDTO updatedPlaylist = playlistService.addTrackToPlaylist(playlistId, request);
        return ResponseEntity.ok(updatedPlaylist);
    }

    /**
     * Xóa một bài hát khỏi playlist.
     * Yêu cầu là chủ sở hữu.
     */
    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlaylistDTO> removeTrackFromPlaylist(@PathVariable Long playlistId, @PathVariable Long trackId) {
        PlaylistDTO updatedPlaylist = playlistService.removeTrackFromPlaylist(playlistId, trackId);
        return ResponseEntity.ok(updatedPlaylist);
    }
}