package com.nouha.user_service.user;

import java.time.LocalDateTime;

public record UserResponse(
        Integer id,
        String name,
        String email,
        LocalDateTime createdAt
) {
}
