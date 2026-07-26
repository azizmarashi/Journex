package org.example.journex.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDto {

    @Size(max = 15)
    private String nickname;

    private String profileImageUrl;

    @Size(max = 13)
    private String phoneNumber;
}