package iuh.fit.MusicStreamAppBackEnd.controller;

import iuh.fit.MusicStreamAppBackEnd.dto.PostDTO;
import iuh.fit.MusicStreamAppBackEnd.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    /**
     * Lấy dòng thời gian (feed) của người dùng đang đăng nhập.
     * Hỗ trợ phân trang, ví dụ: /api/feed?page=0&size=10
     * Yêu cầu đã đăng nhập.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<PostDTO>> getMyFeed(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        Page<PostDTO> feedPage = feedService.getMyFeed(pageable);
        return ResponseEntity.ok(feedPage);
    }
}