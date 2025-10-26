package iuh.fit.MusicStreamAppBackEnd.service;

import iuh.fit.MusicStreamAppBackEnd.dto.SubscriptionDTO;
import iuh.fit.MusicStreamAppBackEnd.dto.SubscriptionPlanDTO;
import iuh.fit.MusicStreamAppBackEnd.entity.Subscription;
import iuh.fit.MusicStreamAppBackEnd.entity.SubscriptionPlan;
import iuh.fit.MusicStreamAppBackEnd.entity.User;
import iuh.fit.MusicStreamAppBackEnd.exception.ResourceNotFoundException;
import iuh.fit.MusicStreamAppBackEnd.exception.UnauthorizedException;
import iuh.fit.MusicStreamAppBackEnd.mapper.ModelMapper;
import iuh.fit.MusicStreamAppBackEnd.repository.SubscriptionPlanRepository;
import iuh.fit.MusicStreamAppBackEnd.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private SubscriptionPlanRepository planRepository;
    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<SubscriptionPlanDTO> getAllPlans() {
        return planRepository.findAll().stream()
                .map(ModelMapper::toSubscriptionPlanDTO) // Cần thêm hàm này vào ModelMapper
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubscriptionDTO getCurrentUserSubscription() {
        User currentUser = userService.getCurrentUser();
        Optional<Subscription> activeSubscription = subscriptionRepository
                .findFirstByUserAndStatusAndEndDateAfterOrderByEndDateDesc(
                        currentUser,
                        Subscription.SubscriptionStatus.ACTIVE,
                        LocalDate.now()
                );

        return activeSubscription.map(ModelMapper::toSubscriptionDTO).orElse(null); // Cần thêm hàm này vào ModelMapper
    }

    /**
     * Hàm này chỉ là giả lập việc đăng ký, thực tế cần tích hợp thanh toán.
     */
    @Transactional
    public SubscriptionDTO subscribeToPlan(Long planId) {
        User currentUser = userService.getCurrentUser();
        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy gói cước với ID: " + planId));

        // Giả sử hủy các gói cũ
        cancelAllActiveSubscriptions(currentUser);

        Subscription newSubscription = new Subscription();
        newSubscription.setUser(currentUser);
        newSubscription.setPlan(plan);
        newSubscription.setStartDate(LocalDate.now());

        // Giả sử gói cước theo tháng
        if (plan.getBillingCycle().equals("MONTHLY")) {
            newSubscription.setEndDate(LocalDate.now().plusMonths(1));
        } else if (plan.getBillingCycle().equals("YEARLY")) {
            newSubscription.setEndDate(LocalDate.now().plusYears(1));
        } else {
            newSubscription.setEndDate(LocalDate.now().plusDays(30)); // Mặc định
        }

        newSubscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);

        Subscription savedSubscription = subscriptionRepository.save(newSubscription);
        return ModelMapper.toSubscriptionDTO(savedSubscription);
    }

    @Transactional
    public void cancelSubscription(Long subscriptionId) {
        User currentUser = userService.getCurrentUser();
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lượt đăng ký với ID: " + subscriptionId));

        if (!subscription.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new UnauthorizedException("Bạn không có quyền hủy gói đăng ký này.");
        }

        subscription.setStatus(Subscription.SubscriptionStatus.CANCELLED);
        subscriptionRepository.save(subscription);
    }

    private void cancelAllActiveSubscriptions(User user) {
        Optional<Subscription> activeSubscription;
        do {
            activeSubscription = subscriptionRepository
                    .findFirstByUserAndStatusAndEndDateAfterOrderByEndDateDesc(
                            user,
                            Subscription.SubscriptionStatus.ACTIVE,
                            LocalDate.now()
                    );
            activeSubscription.ifPresent(sub -> {
                sub.setStatus(Subscription.SubscriptionStatus.CANCELLED);
                subscriptionRepository.save(sub);
            });
        } while (activeSubscription.isPresent());
    }
}