package iuh.fit.MusicStreamAppBackEnd.service;

import iuh.fit.MusicStreamAppBackEnd.dto.AlbumDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.AlbumRequestDTO;
import iuh.fit.MusicStreamAppBackEnd.entity.Album;
import iuh.fit.MusicStreamAppBackEnd.entity.User;
import iuh.fit.MusicStreamAppBackEnd.exception.ResourceNotFoundException;
import iuh.fit.MusicStreamAppBackEnd.exception.UnauthorizedException;
import iuh.fit.MusicStreamAppBackEnd.mapper.ModelMapper;
import iuh.fit.MusicStreamAppBackEnd.repository.AlbumRepository;
import iuh.fit.MusicStreamAppBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public AlbumDTO createAlbum(AlbumRequestDTO request) {
        User currentUser = userService.getCurrentUser();
        Album album = new Album();
        album.setTitle(request.getTitle());
        album.setCoverArtUrl(request.getCoverArtUrl());
        album.setCreator(currentUser);
        album.setReleaseDate(LocalDate.now());

        Album savedAlbum = albumRepository.save(album);
        return ModelMapper.toAlbumDTO(savedAlbum);
    }

    @Transactional(readOnly = true)
    public AlbumDTO getAlbumById(Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Album với ID: " + albumId));

        // Cần map chi tiết, bao gồm cả các track
        AlbumDTO dto = ModelMapper.toAlbumDTO(album);
        dto.setTracks(
                album.getTracks().stream()
                        .map(ModelMapper::toTrackDTO)
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    @Transactional(readOnly = true)
    public List<AlbumDTO> getAlbumsByCreator(Long userId) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId));

        return albumRepository.findByCreator(creator).stream()
                .map(ModelMapper::toAlbumDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AlbumDTO updateAlbumInfo(Long albumId, AlbumRequestDTO request) {
        User currentUser = userService.getCurrentUser();
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Album với ID: " + albumId));

        if (!album.getCreator().getUserId().equals(currentUser.getUserId())) {
            throw new UnauthorizedException("Bạn không có quyền chỉnh sửa album này.");
        }

        album.setTitle(request.getTitle());
        album.setCoverArtUrl(request.getCoverArtUrl());

        Album updatedAlbum = albumRepository.save(album);
        return ModelMapper.toAlbumDTO(updatedAlbum);
    }

    @Transactional
    public void deleteAlbum(Long albumId) {
        User currentUser = userService.getCurrentUser();
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Album với ID: " + albumId));

        if (!album.getCreator().getUserId().equals(currentUser.getUserId())) {
            throw new UnauthorizedException("Bạn không có quyền xóa album này.");
        }

        albumRepository.delete(album);
    }
}