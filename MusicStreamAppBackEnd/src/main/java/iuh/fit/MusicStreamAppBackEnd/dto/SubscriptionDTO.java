package iuh.fit.MusicStreamAppBackEnd.dto;

import iuh.fit.MusicStreamAppBackEnd.entity.Subscription;
import lombok.Data;
import java.time.LocalDate;

@Data
public class SubscriptionDTO {
    private Long subscriptionId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Subscription.SubscriptionStatus status;
    private SubscriptionPlanDTO plan;
}