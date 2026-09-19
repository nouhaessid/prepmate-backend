package com.nouha.user_service.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    /*@GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(
            @PathVariable Integer id
    ){
        return ResponseEntity.ok(userService.findUserById(id));
    }*/

    @PostMapping("/me")
    public ResponseEntity<Integer> createUserIfNotExists(Authentication authentication){
        return ResponseEntity.ok(userService.createUserIfNotExists(authentication));
    }

    /*@PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable Integer id,
            @RequestBody @Valid UserRequest request
    ){
        userService.updateUser(id, request);
        return ResponseEntity.accepted().build();
    }*/
}
