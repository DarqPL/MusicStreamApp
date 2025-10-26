package iuh.fit.MusicStreamAppBackEnd.repository;

import iuh.fit.MusicStreamAppBackEnd.entity.Track;
import iuh.fit.MusicStreamAppBackEnd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

    /**
     * Tìm tất cả các bài hát được tạo bởi một người dùng cụ thể.
     * @param creator Người dùng đã tạo bài hát.
     * @return Danh sách các bài hát.
     */
    List<Track> findByCreator(User creator);

    int countByCreator(User creator);

    List<Track> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}