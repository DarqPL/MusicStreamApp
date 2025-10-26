package iuh.fit.MusicStreamAppBackEnd.service;

import iuh.fit.MusicStreamAppBackEnd.dto.UserDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.UserProfileDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.UserProfileUpdateDTO;
import iuh.fit.MusicStreamAppBackEnd.entity.User;
import iuh.fit.MusicStreamAppBackEnd.exception.ResourceNotFoundException;
import iuh.fit.MusicStreamAppBackEnd.mapper.ModelMapper;
import iuh.fit.MusicStreamAppBackEnd.repository.TrackRepository;
import iuh.fit.MusicStreamAppBackEnd.repository.UserRepository;
import iuh.fit.MusicStreamAppBackEnd.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TrackRepository trackRepository;

    /**
     * Phương thức cốt lõi: Lấy entity User của người dùng đang đăng nhập.
     * @return Entity User
     */
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với username: " + username));
    }

    /**
     * Lấy thông tin profile của một user bất kỳ bằng ID.
     */
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId));

        // Đếm số lượng track (ví dụ về cách lấy thêm dữ liệu)
        int trackCount = trackRepository.countByCreator(user);

        UserProfileDTO profileDTO = ModelMapper.toUserProfileDTO(user, trackCount);

        // Ẩn email nếu người xem không phải là chính chủ
        try {
            if (!getCurrentUser().getUserId().equals(userId)) {
                profileDTO.setEmail(null);
            }
        } catch (Exception e) {
            // Trường hợp người xem chưa đăng nhập
            profileDTO.setEmail(null);
        }

        return profileDTO;
    }

    @Transactional
    public UserDTO updateUserProfile(UserProfileUpdateDTO updateDTO) {
        User currentUser = getCurrentUser();

        if (updateDTO.getBio() != null) {
            currentUser.setBio(updateDTO.getBio());
        }
        if (updateDTO.getProfilePictureUrl() != null) {
            currentUser.setProfilePictureUrl(updateDTO.getProfilePictureUrl());
        }

        User savedUser = userRepository.save(currentUser);
        return ModelMapper.toUserDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(ModelMapper::toUserDTO);
    }
}