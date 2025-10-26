package iuh.fit.MusicStreamAppBackEnd.controller;

import iuh.fit.MusicStreamAppBackEnd.dto.CommentDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.CommentRequestDTO;
import iuh.fit.MusicStreamAppBackEnd.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * Lấy tất cả bình luận (cấp 1) của một bài post.
     * Công khai.
     */
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsForPost(@PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getCommentsForPost(postId);
        return ResponseEntity.ok(comments);
    }

    /**
     * Lấy các bình luận trả lời (cấp 2) của một bình luận cha.
     * Công khai.
     */
    @GetMapping("/comments/{commentId}/replies")
    public ResponseEntity<List<CommentDTO>> getCommentReplies(@PathVariable Long commentId) {
        List<CommentDTO> replies = commentService.getCommentReplies(commentId);
        return ResponseEntity.ok(replies);
    }

    /**
     * Tạo một bình luận mới (hoặc trả lời) cho một bài post.
     * Yêu cầu đã đăng nhập.
     */
    @PostMapping("/posts/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long postId, @Valid @RequestBody CommentRequestDTO request) {
        CommentDTO newComment = commentService.createComment(postId, request);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    /**
     * Xóa một bình luận.
     * Yêu cầu là chủ sở hữu bình luận hoặc chủ bài post.
     */
    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}