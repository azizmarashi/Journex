package org.example.journex.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.journex.enums.SubscriptionPlan;
import org.example.journex.enums.SubscriptionStatus;
import org.example.journex.enums.UserRole;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String profileImageUrl;

    @NotNull
    private String nickname;

    @NotNull
    private String username;

    @NotNull
    private String email;

    private String phoneNumber;

    @NotNull
    private Boolean enabled;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastLoginAt;

    private LocalDateTime subscriptionExpireAt;

    private List<Long> strategyIds;

    private SubscriptionPlan subscriptionPlan;

    private SubscriptionStatus subscriptionStatus;

    private UserRole role;

}