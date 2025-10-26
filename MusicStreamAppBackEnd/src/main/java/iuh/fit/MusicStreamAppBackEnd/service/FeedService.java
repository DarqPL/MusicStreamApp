package iuh.fit.MusicStreamAppBackEnd.service;

import iuh.fit.MusicStreamAppBackEnd.dto.PostDTO;
import iuh.fit.MusicStreamAppBackEnd.entity.Post;
import iuh.fit.MusicStreamAppBackEnd.entity.User;
import iuh.fit.MusicStreamAppBackEnd.mapper.ModelMapper;
import iuh.fit.MusicStreamAppBackEnd.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public Page<PostDTO> getMyFeed(Pageable pageable) {
        User currentUser = userService.getCurrentUser();

        // Lấy danh sách những người mà user này đang theo dõi
        List<User> usersToFetchPostsFrom = new ArrayList<>(currentUser.getFollowing());

        // Thêm cả chính user hiện tại vào danh sách
        // (để họ thấy cả bài đăng của chính mình trên feed)
        usersToFetchPostsFrom.add(currentUser);

        // Lấy các bài post từ danh sách user này, đã được phân trang
        Page<Post> postPage = postRepository.findByUserInOrderByCreatedAtDesc(usersToFetchPostsFrom, pageable);

        // Chuyển đổi Page<Post> sang Page<PostDTO>
        return postPage.map(post -> ModelMapper.toPostDTO(post, currentUser));
    }
}