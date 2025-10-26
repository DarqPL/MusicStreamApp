package iuh.fit.MusicStreamAppBackEnd.controller;

import iuh.fit.MusicStreamAppBackEnd.dto.PostDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.PostRequestDTO;
import iuh.fit.MusicStreamAppBackEnd.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Lấy thông tin chi tiết một bài post.
     * Công khai.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        PostDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    /**
     * Tạo một bài post mới (NEW_TRACK hoặc REPOST).
     * Yêu cầu đã đăng nhập.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostRequestDTO request) {
        PostDTO newPost = postService.createPost(request);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    /**
     * Xóa một bài post.
     * Yêu cầu là chủ sở hữu.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id); // Cần thêm hàm này vào PostService
        return ResponseEntity.noContent().build();
    }

    /**
     * Like một bài post.
     */
    @PostMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> likePost(@PathVariable Long id) {
        postService.likePost(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Bỏ like một bài post.
     */
    @DeleteMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unlikePost(@PathVariable Long id) {
        postService.unlikePost(id);
        return ResponseEntity.noContent().build();
    }
}