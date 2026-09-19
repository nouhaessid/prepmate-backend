package com.nouha.user_service.user;

import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
