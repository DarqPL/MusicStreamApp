package iuh.fit.MusicStreamAppBackEnd.repository;

import iuh.fit.MusicStreamAppBackEnd.entity.Post;
import iuh.fit.MusicStreamAppBackEnd.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Tìm tất cả các bài đăng của một người dùng, sắp xếp theo thời gian mới nhất.
     * @param user Người dùng đã đăng bài.
     * @return Danh sách các bài đăng.
     */
    List<Post> findByUserOrderByCreatedAtDesc(User user);

    /**
     * Tìm các bài đăng từ một danh sách người dùng,
     * sắp xếp theo thời gian mới nhất (hỗ trợ phân trang).
     */
    Page<Post> findByUserInOrderByCreatedAtDesc(List<User> users, Pageable pageable);
}