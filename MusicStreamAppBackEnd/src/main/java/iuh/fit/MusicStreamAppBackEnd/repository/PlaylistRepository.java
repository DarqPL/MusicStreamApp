package iuh.fit.MusicStreamAppBackEnd.repository;

import iuh.fit.MusicStreamAppBackEnd.entity.Playlist;
import iuh.fit.MusicStreamAppBackEnd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    /**
     * Tìm tất cả các playlist được tạo bởi một người dùng cụ thể.
     * @param user Người dùng đã tạo playlist.
     * @return Danh sách các playlist.
     */
    List<Playlist> findByUser(User user);
}