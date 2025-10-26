package iuh.fit.MusicStreamAppBackEnd.service;

import iuh.fit.MusicStreamAppBackEnd.dto.SearchResultDTO;
import iuh.fit.MusicStreamAppBackEnd.mapper.ModelMapper;
import iuh.fit.MusicStreamAppBackEnd.repository.AlbumRepository;
import iuh.fit.MusicStreamAppBackEnd.repository.TrackRepository;
import iuh.fit.MusicStreamAppBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AlbumRepository albumRepository;

    @Transactional(readOnly = true)
    public SearchResultDTO search(String query) {
        // Giới hạn kết quả tìm kiếm (ví dụ: 5 kết quả mỗi loại)
        Pageable limit = PageRequest.of(0, 5);

        SearchResultDTO results = new SearchResultDTO();

        results.setTracks(
                trackRepository.findByTitleContainingIgnoreCase(query, limit).stream()
                        .map(ModelMapper::toTrackDTO)
                        .collect(Collectors.toList())
        );

        results.setCreators(
                userRepository.findByUsernameContainingIgnoreCase(query, limit).stream()
                        .map(ModelMapper::toUserDTO)
                        .collect(Collectors.toList())
        );

        results.setAlbums(
                albumRepository.findByTitleContainingIgnoreCase(query, limit).stream()
                        .map(ModelMapper::toAlbumDTO)
                        .collect(Collectors.toList())
        );

        return results;
    }
}