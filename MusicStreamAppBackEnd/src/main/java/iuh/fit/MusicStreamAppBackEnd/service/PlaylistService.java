package iuh.fit.MusicStreamAppBackEnd.service;

import iuh.fit.MusicStreamAppBackEnd.dto.PlaylistDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.PlaylistRequestDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.PlaylistTrackRequestDTO;
import iuh.fit.MusicStreamAppBackEnd.entity.Playlist;
import iuh.fit.MusicStreamAppBackEnd.entity.PlaylistTrack;
import iuh.fit.MusicStreamAppBackEnd.entity.Track;
import iuh.fit.MusicStreamAppBackEnd.entity.User;
import iuh.fit.MusicStreamAppBackEnd.exception.ResourceNotFoundException;
import iuh.fit.MusicStreamAppBackEnd.exception.UnauthorizedException;
import iuh.fit.MusicStreamAppBackEnd.mapper.ModelMapper;
import iuh.fit.MusicStreamAppBackEnd.repository.PlaylistRepository;
import iuh.fit.MusicStreamAppBackEnd.repository.PlaylistTrackRepository;
import iuh.fit.MusicStreamAppBackEnd.repository.TrackRepository;
import iuh.fit.MusicStreamAppBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private PlaylistTrackRepository playlistTrackRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PlaylistDTO createPlaylist(PlaylistRequestDTO request) {
        User currentUser = userService.getCurrentUser();
        Playlist playlist = new Playlist();
        playlist.setName(request.getName());
        playlist.setUser(currentUser);
        Playlist savedPlaylist = playlistRepository.save(playlist);
        return ModelMapper.toPlaylistDTO(savedPlaylist);
    }

    @Transactional(readOnly = true)
    public PlaylistDTO getPlaylistById(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Playlist với ID: " + playlistId));
        return ModelMapper.toPlaylistDTO(playlist);
    }

    @Transactional
    public PlaylistDTO addTrackToPlaylist(Long playlistId, PlaylistTrackRequestDTO request) {
        User currentUser = userService.getCurrentUser();
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Playlist với ID: " + playlistId));

        if (!playlist.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new UnauthorizedException("Bạn không có quyền chỉnh sửa playlist này.");
        }

        Track track = trackRepository.findById(request.getTrackId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Track với ID: " + request.getTrackId()));

        PlaylistTrack playlistTrack = new PlaylistTrack();
        playlistTrack.setPlaylist(playlist);
        playlistTrack.setTrack(track);
        // Gán thứ tự: là bài hát cuối cùng trong danh sách
        playlistTrack.setTrackOrder(playlist.getTracks().size());

        playlistTrackRepository.save(playlistTrack);

        // Tải lại playlist để lấy dữ liệu mới nhất
        Playlist updatedPlaylist = playlistRepository.findById(playlistId).get();
        return ModelMapper.toPlaylistDTO(updatedPlaylist);
    }

    @Transactional
    public PlaylistDTO removeTrackFromPlaylist(Long playlistId, Long trackId) {
        User currentUser = userService.getCurrentUser();
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Playlist với ID: " + playlistId));

        if (!playlist.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new UnauthorizedException("Bạn không có quyền chỉnh sửa playlist này.");
        }

        // Tìm bản ghi PlaylistTrack tương ứng
        PlaylistTrack playlistTrackToRemove = playlist.getTracks().stream()
                .filter(pt -> pt.getTrack().getTrackId().equals(trackId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Bài hát không có trong playlist này."));

        // Xóa khỏi danh sách và khỏi CSDL
        playlist.getTracks().remove(playlistTrackToRemove);
        playlistTrackRepository.delete(playlistTrackToRemove);

        // Cần cập nhật lại trackOrder cho các bài hát còn lại (logic này có thể phức tạp)
        // Tạm thời chỉ xóa

        Playlist savedPlaylist = playlistRepository.save(playlist);
        return ModelMapper.toPlaylistDTO(savedPlaylist);
    }

    @Transactional
    public PlaylistDTO updatePlaylistName(Long playlistId, PlaylistRequestDTO request) {
        User currentUser = userService.getCurrentUser();
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Playlist với ID: " + playlistId));

        if (!playlist.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new UnauthorizedException("Bạn không có quyền chỉnh sửa playlist này.");
        }

        playlist.setName(request.getName());
        Playlist updatedPlaylist = playlistRepository.save(playlist);
        return ModelMapper.toPlaylistDTO(updatedPlaylist);
    }

    @Transactional
    public void deletePlaylist(Long playlistId) {
        User currentUser = userService.getCurrentUser();
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Playlist với ID: " + playlistId));

        if (!playlist.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new UnauthorizedException("Bạn không có quyền xóa playlist này.");
        }

        playlistRepository.delete(playlist);
    }

    @Transactional(readOnly = true)
    public List<PlaylistDTO> getPlaylistsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy User với ID: " + userId));

        return playlistRepository.findByUser(user).stream()
                .map(ModelMapper::toPlaylistDTO)
                .collect(Collectors.toList());
    }
}