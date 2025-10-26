package iuh.fit.MusicStreamAppBackEnd.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SubscriptionPlanDTO {
    private Long planId;
    private String name;
    private BigDecimal price;
    private String billingCycle; // Ví dụ: "MONTHLY", "YEARLY"
}