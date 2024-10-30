package com.example.monolith.web.model;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {
    
    private UUID id;

    @NotBlank(message = "{user.username.notBlank}")
    @Size(min = 3, max = 50, message = "{user.username.size}")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,50}$", message = "{user.username.pattern}")
    private String username;
}
