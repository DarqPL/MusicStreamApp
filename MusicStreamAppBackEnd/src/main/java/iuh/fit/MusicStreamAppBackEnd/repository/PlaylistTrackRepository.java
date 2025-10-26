package iuh.fit.MusicStreamAppBackEnd.repository;

import iuh.fit.MusicStreamAppBackEnd.entity.PlaylistTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistTrackRepository extends JpaRepository<PlaylistTrack, Long> {
}