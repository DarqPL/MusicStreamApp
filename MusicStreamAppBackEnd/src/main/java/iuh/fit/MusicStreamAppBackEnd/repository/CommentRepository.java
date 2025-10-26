package iuh.fit.MusicStreamAppBackEnd.repository;

import iuh.fit.MusicStreamAppBackEnd.entity.Comment;
import iuh.fit.MusicStreamAppBackEnd.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Tìm tất cả các bình luận của một bài đăng, sắp xếp theo thời gian cũ nhất.
     * @param post Bài đăng cần tìm bình luận.
     * @return Danh sách các bình luận.
     */
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
    List<Comment> findByPostAndParentCommentIsNullOrderByCreatedAtAsc(Post post);
    List<Comment> findByParentCommentOrderByCreatedAtAsc(Comment parentComment);
}