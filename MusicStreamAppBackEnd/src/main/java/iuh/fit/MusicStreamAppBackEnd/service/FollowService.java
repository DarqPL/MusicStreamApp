package iuh.fit.MusicStreamAppBackEnd.service;

import iuh.fit.MusicStreamAppBackEnd.entity.User;
import iuh.fit.MusicStreamAppBackEnd.exception.ResourceNotFoundException;
import iuh.fit.MusicStreamAppBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public void followUser(Long userIdToFollow) {
        User currentUser = userService.getCurrentUser();
        User userToFollow = userRepository.findById(userIdToFollow)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng để follow: " + userIdToFollow));

        if (currentUser.getUserId().equals(userIdToFollow)) {
            throw new IllegalArgumentException("Bạn không thể tự theo dõi chính mình.");
        }

        // Cập nhật cả 2 phía của mối quan hệ
        currentUser.getFollowing().add(userToFollow);
        userToFollow.getFollowers().add(currentUser);

        userRepository.save(currentUser);
        userRepository.save(userToFollow);
    }

    @Transactional
    public void unfollowUser(Long userIdToUnfollow) {
        User currentUser = userService.getCurrentUser();
        User userToUnfollow = userRepository.findById(userIdToUnfollow)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng để unfollow: " + userIdToUnfollow));

        currentUser.getFollowing().remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(currentUser);

        userRepository.save(currentUser);
        userRepository.save(userToUnfollow);
    }
}