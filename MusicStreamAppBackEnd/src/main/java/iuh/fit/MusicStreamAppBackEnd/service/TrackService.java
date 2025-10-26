package iuh.fit.MusicStreamAppBackEnd.service;

import com.mpatric.mp3agic.UnsupportedTagException;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import iuh.fit.MusicStreamAppBackEnd.dto.TrackDTO;
import iuh.fit.MusicStreamAppBackEnd.entity.Album;
import iuh.fit.MusicStreamAppBackEnd.entity.Track;
import iuh.fit.MusicStreamAppBackEnd.entity.User;
import iuh.fit.MusicStreamAppBackEnd.exception.ResourceNotFoundException;
import iuh.fit.MusicStreamAppBackEnd.exception.UnauthorizedException;
import iuh.fit.MusicStreamAppBackEnd.mapper.ModelMapper;
import iuh.fit.MusicStreamAppBackEnd.repository.AlbumRepository;
import iuh.fit.MusicStreamAppBackEnd.repository.TrackRepository;
import iuh.fit.MusicStreamAppBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class TrackService {

    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService; // Để lấy user hiện tại
    @Autowired
    private FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public TrackDTO getTrackById(Long trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài hát với ID: " + trackId));
        return ModelMapper.toTrackDTO(track);
    }

    @Transactional
    public TrackDTO uploadTrack(MultipartFile file, String title, Long albumId) {
        User currentUser = userService.getCurrentUser();

        String fileUrl;
        int durationInSeconds;
        File tempFile = null; // Biến để giữ file tạm

        try {
            // 1. CHUYỂN MultipartFile SANG File TẠM
            tempFile = convertMultiPartToFile(file);

            // 2. TỰ ĐỘNG ĐỌC DURATION TỪ FILE TẠM
            // Bây giờ chúng ta dùng constructor Mp3File(File)
            Mp3File mp3file = new Mp3File(tempFile);
            durationInSeconds = (int) mp3file.getLengthInSeconds();

            // 3. UPLOAD LÊN CLOUDINARY
            // Dùng file.getBytes() để tránh phải đọc lại file tạm
            fileUrl = fileStorageService.uploadAudioFile(file.getBytes());

        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            throw new RuntimeException("Lỗi khi xử lý file: " + e.getMessage());
        } finally {
            // 4. (RẤT QUAN TRỌNG) XÓA FILE TẠM SAU KHI DÙNG XONG
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }

        // 5. TẠO TRACK MỚI
        Track track = new Track();
        track.setTitle(title);
        track.setDuration(durationInSeconds);
        track.setAudioFileUrl(fileUrl);
        track.setCreator(currentUser);
        track.setReleaseDate(LocalDate.now());
        track.setPlayCount(0);

        // 6. (Tùy chọn) Gán vào album
        if (albumId != null) {
            Album album = albumRepository.findById(albumId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Album với ID: " + albumId));

            if (!album.getCreator().getUserId().equals(currentUser.getUserId())) {
                throw new UnauthorizedException("Bạn không sở hữu album này.");
            }
            track.setAlbum(album);
        }

        // 7. LƯU VÀO CSDL
        Track savedTrack = trackRepository.save(track);
        return ModelMapper.toTrackDTO(savedTrack);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        // Tạo một file tạm trong thư mục tạm của hệ thống
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    @Transactional
    public void likeTrack(Long trackId) {
        User currentUser = userService.getCurrentUser();
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài hát với ID: " + trackId));

        currentUser.getLikedTracks().add(track);
        userRepository.save(currentUser);
    }

    @Transactional
    public void unlikeTrack(Long trackId) {
        User currentUser = userService.getCurrentUser();
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài hát với ID: " + trackId));

        currentUser.getLikedTracks().remove(track);
        userRepository.save(currentUser);
    }

    @Transactional
    public void deleteTrackByAdmin(Long trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài hát với ID: " + trackId));
        // Admin có quyền xóa bất kỳ bài hát nào mà không cần kiểm tra chủ sở hữu
        trackRepository.delete(track);
    }
}