package iuh.fit.MusicStreamAppBackEnd.controller;

import iuh.fit.MusicStreamAppBackEnd.dto.UserDTO;
import iuh.fit.MusicStreamAppBackEnd.service.PostService;
import iuh.fit.MusicStreamAppBackEnd.service.TrackService;
import iuh.fit.MusicStreamAppBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')") // <-- KHÓA CẤP ĐỘ CAO NHẤT
// Chỉ những ai có vai trò "ROLE_ADMIN" mới có thể gọi bất kỳ API nào trong Controller này
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private PostService postService;

    /**
     * Lấy danh sách tất cả người dùng (hỗ trợ phân trang).
     * API: GET /api/admin/users?page=0&size=20
     */
    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(@PageableDefault(size = 20) Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * Xóa một bài hát (Track) khỏi hệ thống.
     * API: DELETE /api/admin/tracks/{id}
     */
    @DeleteMapping("/tracks/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
        trackService.deleteTrackByAdmin(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Xóa một bài đăng (Post) khỏi hệ thống.
     * API: DELETE /api/admin/posts/{id}
     */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id); // Admin cũng có thể xóa post
        return ResponseEntity.noContent().build();
    }
}