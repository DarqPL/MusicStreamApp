package iuh.fit.MusicStreamAppBackEnd.service;

import iuh.fit.MusicStreamAppBackEnd.dto.CommentDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.CommentRequestDTO;
import iuh.fit.MusicStreamAppBackEnd.entity.Comment;
import iuh.fit.MusicStreamAppBackEnd.entity.Post;
import iuh.fit.MusicStreamAppBackEnd.entity.User;
import iuh.fit.MusicStreamAppBackEnd.exception.ResourceNotFoundException;
import iuh.fit.MusicStreamAppBackEnd.exception.UnauthorizedException;
import iuh.fit.MusicStreamAppBackEnd.mapper.ModelMapper;
import iuh.fit.MusicStreamAppBackEnd.repository.CommentRepository;
import iuh.fit.MusicStreamAppBackEnd.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public CommentDTO createComment(Long postId, CommentRequestDTO request) {
        User currentUser = userService.getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Post với ID: " + postId));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(currentUser);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());

        if (request.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bình luận cha với ID: " + request.getParentCommentId()));
            comment.setParentComment(parentComment);
        }

        Comment savedComment = commentRepository.save(comment);
        return ModelMapper.toCommentDTO(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Post với ID: " + postId));

        // Chỉ lấy các bình luận gốc (không có parent)
        List<Comment> topLevelComments = commentRepository.findByPostAndParentCommentIsNullOrderByCreatedAtAsc(post);

        return topLevelComments.stream()
                .map(ModelMapper::toCommentDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentReplies(Long commentId) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bình luận với ID: " + commentId));

        List<Comment> replies = commentRepository.findByParentCommentOrderByCreatedAtAsc(parentComment);

        return replies.stream()
                .map(ModelMapper::toCommentDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long commentId) {
        User currentUser = userService.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bình luận với ID: " + commentId));

        // Chỉ chủ bình luận hoặc chủ bài đăng mới được xóa
        if (!comment.getUser().getUserId().equals(currentUser.getUserId()) &&
                !comment.getPost().getUser().getUserId().equals(currentUser.getUserId())) {
            throw new UnauthorizedException("Bạn không có quyền xóa bình luận này.");
        }

        commentRepository.delete(comment);
    }
}