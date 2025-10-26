package iuh.fit.MusicStreamAppBackEnd.controller;

import iuh.fit.MusicStreamAppBackEnd.dto.AlbumDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.AlbumRequestDTO;
import iuh.fit.MusicStreamAppBackEnd.service.AlbumService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    /**
     * Lấy thông tin chi tiết một album (bao gồm cả các track).
     * Công khai.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> getAlbumById(@PathVariable Long id) {
        AlbumDTO album = albumService.getAlbumById(id);
        return ResponseEntity.ok(album);
    }

    /**
     * Lấy danh sách album của một người dùng.
     * Công khai.
     */
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<AlbumDTO>> getAlbumsByUserId(@PathVariable Long userId) {
        List<AlbumDTO> albums = albumService.getAlbumsByCreator(userId);
        return ResponseEntity.ok(albums);
    }

    /**
     * Tạo một album mới.
     * Yêu cầu đã đăng nhập.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AlbumDTO> createAlbum(@Valid @RequestBody AlbumRequestDTO request) {
        AlbumDTO newAlbum = albumService.createAlbum(request);
        return new ResponseEntity<>(newAlbum, HttpStatus.CREATED);
    }

    /**
     * Cập nhật thông tin một album.
     * Yêu cầu là chủ sở hữu.
     */
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AlbumDTO> updateAlbum(@PathVariable Long id, @Valid @RequestBody AlbumRequestDTO request) {
        AlbumDTO updatedAlbum = albumService.updateAlbumInfo(id, request);
        return ResponseEntity.ok(updatedAlbum);
    }

    /**
     * Xóa một album.
     * Yêu cầu là chủ sở hữu.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        return ResponseEntity.noContent().build();
    }
}