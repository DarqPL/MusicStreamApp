package iuh.fit.MusicStreamAppBackEnd.controller;

import iuh.fit.MusicStreamAppBackEnd.dto.TrackDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.TrackUploadDTO;
import iuh.fit.MusicStreamAppBackEnd.service.TrackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    @Autowired
    private TrackService trackService;

    /**
     * Lấy thông tin chi tiết một bài hát bằng ID.
     * Công khai cho mọi người.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrackDTO> getTrackById(@PathVariable Long id) {
        TrackDTO track = trackService.getTrackById(id);
        return ResponseEntity.ok(track);
    }

    /**
     * Endpoint để tải lên một bài hát mới.
     * Nhận vào form-data, tự động đọc duration, không cần description.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TrackDTO> uploadTrack(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "albumId", required = false) Long albumId) {

        // Gọi service đã được cập nhật
        TrackDTO newTrack = trackService.uploadTrack(file, title, albumId);
        return new ResponseEntity<>(newTrack, HttpStatus.CREATED);
    }

    /**
     * Like một bài hát.
     */
    @PostMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> likeTrack(@PathVariable Long id) {
        trackService.likeTrack(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Bỏ like một bài hát.
     */
    @DeleteMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unlikeTrack(@PathVariable Long id) {
        trackService.unlikeTrack(id);
        return ResponseEntity.noContent().build();
    }


}