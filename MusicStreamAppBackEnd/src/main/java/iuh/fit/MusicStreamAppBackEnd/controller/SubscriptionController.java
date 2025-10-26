package iuh.fit.MusicStreamAppBackEnd.controller;

import iuh.fit.MusicStreamAppBackEnd.dto.SubscriptionDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.SubscriptionPlanDTO;
import iuh.fit.MusicStreamAppBackEnd.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * Lấy danh sách tất cả các gói cước có sẵn.
     * Công khai.
     */
    @GetMapping("/plans")
    public ResponseEntity<List<SubscriptionPlanDTO>> getAllPlans() {
        List<SubscriptionPlanDTO> plans = subscriptionService.getAllPlans();
        return ResponseEntity.ok(plans);
    }

    /**
     * Lấy gói cước đang hoạt động của người dùng.
     * Yêu cầu đã đăng nhập.
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SubscriptionDTO> getMySubscription() {
        SubscriptionDTO subscription = subscriptionService.getCurrentUserSubscription();
        return ResponseEntity.ok(subscription);
    }

    /**
     * Đăng ký một gói cước mới (Giả lập thanh toán).
     * Yêu cầu đã đăng nhập.
     */
    @PostMapping("/subscribe")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SubscriptionDTO> subscribe(@RequestBody Map<String, Long> payload) {
        Long planId = payload.get("planId");
        SubscriptionDTO newSubscription = subscriptionService.subscribeToPlan(planId);
        return ResponseEntity.ok(newSubscription);
    }

    /**
     * Hủy một gói cước.
     * Yêu cầu đã đăng nhập.
     */
    @PostMapping("/{subscriptionId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long subscriptionId) {
        subscriptionService.cancelSubscription(subscriptionId);
        return ResponseEntity.ok().build();
    }
}