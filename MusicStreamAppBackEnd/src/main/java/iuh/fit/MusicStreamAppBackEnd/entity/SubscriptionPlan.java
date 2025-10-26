package iuh.fit.MusicStreamAppBackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "subscription_plan")
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    private String name;
    private BigDecimal price;
    @Column(name = "billing_cycle")
    @Enumerated(EnumType.STRING)
    private SubscriptionPlanBillingCycle billingCycle;

    public enum SubscriptionPlanBillingCycle{
        MONTHLY,YEARLY
    }
}