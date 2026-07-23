package org.example.journex.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotNull
    private String username;

    @NotNull
    private String nickname;

    @NotNull
    private String email;

    @NotNull
    private String password;

}
