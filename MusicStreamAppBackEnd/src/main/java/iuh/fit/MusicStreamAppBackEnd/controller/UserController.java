package iuh.fit.MusicStreamAppBackEnd.controller;

import iuh.fit.MusicStreamAppBackEnd.dto.UserDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.UserProfileDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.UserProfileUpdateDTO;
import iuh.fit.MusicStreamAppBackEnd.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Lấy thông tin profile chi tiết của một user bằng ID.
     * Bất kỳ ai cũng có thể xem (đã đăng nhập hoặc chưa).
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long id) {
        UserProfileDTO userProfile = userService.getUserProfile(id);
        return ResponseEntity.ok(userProfile);
    }

    /**
     * Lấy thông tin profile của chính người dùng đang đăng nhập.
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") // Yêu cầu đã đăng nhập
    public ResponseEntity<UserProfileDTO> getMyProfile() {
        // Lấy user từ service (đã được xác thực)
        Long currentUserId = userService.getCurrentUser().getUserId();
        UserProfileDTO userProfile = userService.getUserProfile(currentUserId);
        return ResponseEntity.ok(userProfile);
    }

    /**
     * Cập nhật thông tin (bio, ảnh đại diện) của người dùng đang đăng nhập.
     */
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> updateMyProfile(@Valid @RequestBody UserProfileUpdateDTO updateDTO) {
        UserDTO updatedUser = userService.updateUserProfile(updateDTO);
        return ResponseEntity.ok(updatedUser);
    }
}