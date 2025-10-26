package iuh.fit.MusicStreamAppBackEnd.service;

import iuh.fit.MusicStreamAppBackEnd.dto.PostDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.PostRequestDTO;
import iuh.fit.MusicStreamAppBackEnd.entity.Post;
import iuh.fit.MusicStreamAppBackEnd.entity.Track;
import iuh.fit.MusicStreamAppBackEnd.entity.User;
import iuh.fit.MusicStreamAppBackEnd.exception.ResourceNotFoundException;
import iuh.fit.MusicStreamAppBackEnd.exception.UnauthorizedException;
import iuh.fit.MusicStreamAppBackEnd.mapper.ModelMapper;
import iuh.fit.MusicStreamAppBackEnd.repository.PostRepository;
import iuh.fit.MusicStreamAppBackEnd.repository.TrackRepository;
import iuh.fit.MusicStreamAppBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public PostDTO createPost(PostRequestDTO request) {
        User currentUser = userService.getCurrentUser();
        Post post = new Post();
        post.setUser(currentUser);
        post.setPostType(request.getPostType());
        post.setCreatedAt(LocalDateTime.now());

        switch (request.getPostType()) {
            case NEW_TRACK:
                Track track = trackRepository.findById(request.getTrackId())
                        .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
                post.setTrack(track);
                break;
            case REPOST:
                Post originalPost = postRepository.findById(request.getOriginalPostId())
                        .orElseThrow(() -> new ResourceNotFoundException("Original post not found"));
                post.setOriginalPost(originalPost);
                break;
        }

        Post savedPost = postRepository.save(post);
        return ModelMapper.toPostDTO(savedPost, currentUser);
    }

    @Transactional(readOnly = true)
    public PostDTO getPostById(Long postId) {
        User currentUser = null;
        try {
            currentUser = userService.getCurrentUser();
        } catch (Exception e) {
            // Cho phép người dùng vãng lai xem post
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return ModelMapper.toPostDTO(post, currentUser);
    }

    @Transactional
    public void likePost(Long postId) {
        User currentUser = userService.getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        currentUser.getLikedPosts().add(post);
        userRepository.save(currentUser);
    }

    @Transactional
    public void unlikePost(Long postId) {
        User currentUser = userService.getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        currentUser.getLikedPosts().remove(post);
        userRepository.save(currentUser);
    }

    @Transactional
    public void deletePost(Long postId) {
        User currentUser = userService.getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Post với ID: " + postId));

        // Kiểm tra quyền sở hữu
        if (!post.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new UnauthorizedException("Bạn không có quyền xóa bài đăng này.");
        }

        postRepository.delete(post);
    }
}