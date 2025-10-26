package iuh.fit.MusicStreamAppBackEnd.controller;

import iuh.fit.MusicStreamAppBackEnd.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class FollowController {

    @Autowired
    private FollowService followService;

    /**
     * Theo dõi một người dùng.
     */
    @PostMapping("/{id}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> followUser(@PathVariable Long id) {
        followService.followUser(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Bỏ theo dõi một người dùng.
     */
    @DeleteMapping("/{id}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long id) {
        followService.unfollowUser(id);
        return ResponseEntity.noContent().build();
    }
}