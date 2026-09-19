package com.nouha.user_service.user;

import com.nouha.user_service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserResponse findUserById(Integer id) {

        return userRepository.findById(id)
                .map(mapper::toUserResponse)
                .orElseThrow(() -> new UserNotFoundException(String.format("No user was found with the provided ID:: %s", id)));
    }

    public Integer createUserIfNotExists(Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getPrincipal();

        String keycloakUserId = jwt.getSubject();

        return userRepository.findByKeycloakUserId(keycloakUserId)
                .map(User::getId)
                .orElseGet(() -> {
                    User user = User.builder()
                            .keycloakUserId(keycloakUserId)
                            .name(jwt.getClaimAsString("name"))
                            .email(jwt.getClaimAsString("email"))
                            .createdAt(LocalDateTime.now())
                            .build();
                    return  userRepository.save(user).getId();
                });
    }

    public void updateUser(Integer id, UserRequest request) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("No user was found with the provided ID:: %s", id)));
        user.setName(request.name());
        userRepository.save(user);
    }
}
