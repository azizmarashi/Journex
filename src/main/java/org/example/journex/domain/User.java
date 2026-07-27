package org.example.journex.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.SubscriptionPlan;
import org.example.journex.enums.SubscriptionStatus;
import org.example.journex.enums.UserRole;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "user_tb" , schema = "journex_db")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @NotNull
    @Column(name = "nickname", length = 15)
    private String nickname;

    @NotNull
    @Column(name = "username", unique = true, length = 15)
    private String username;

    @NotNull
    @Column(name = "email", unique = true, length = 40)
    private String email;

    @Column(name = "phone_number", length = 13)
    private String phoneNumber;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "enabled")
    private Boolean enabled;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Strategy> strategies;

    @Column(name = "subscription_plan")
    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "subscription_status")
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Column(name = "subscription_expire_at")
    private LocalDateTime subscriptionExpireAt;

}