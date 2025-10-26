package iuh.fit.MusicStreamAppBackEnd.repository;

import iuh.fit.MusicStreamAppBackEnd.entity.Album;
import iuh.fit.MusicStreamAppBackEnd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    /**
     * Tìm tất cả các album được tạo bởi một người dùng cụ thể.
     * @param creator Người dùng đã tạo album.
     * @return Danh sách các album.
     */
    List<Album> findByCreator(User creator);

    List<Album> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}