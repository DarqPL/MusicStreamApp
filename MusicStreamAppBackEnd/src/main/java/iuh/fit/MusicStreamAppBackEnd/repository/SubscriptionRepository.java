package iuh.fit.MusicStreamAppBackEnd.repository;

import iuh.fit.MusicStreamAppBackEnd.entity.Subscription;
import iuh.fit.MusicStreamAppBackEnd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * Tìm kiếm một gói đăng ký đang hoạt động của người dùng mà vẫn còn hạn sử dụng.
     * @param user Người dùng cần kiểm tra.
     * @param status Trạng thái cần tìm (thường là ACTIVE).
     * @param now Thời gian hiện tại, để so sánh với ngày hết hạn.
     * @return Optional chứa Subscription nếu tìm thấy.
     */
    Optional<Subscription> findFirstByUserAndStatusAndEndDateAfterOrderByEndDateDesc(
            User user,
            Subscription.SubscriptionStatus status,
            LocalDate now
    );
}