package com.ems.controller;

import com.ems.dto.response.MessageResponse;
import com.ems.dto.response.UserResponse;
import com.ems.jwt.UserDetailsImpl;
import com.ems.model.User;
import com.ems.repository.UserRepository;
import com.ems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ATTENDEE') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getMyProfile(){
        User user=getCurrentUser();
        return ResponseEntity.ok(userService.getUserById(user.getId()));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('ATTENDEE') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody Map<String, String> profileData) {
        User user = getCurrentUser();
        UserResponse response = userService.updateUserProfile(
                user.getId(),
                profileData.get("firstName"),
                profileData.get("lastName"),
                profileData.get("phoneNumber")
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasRole('ATTENDEE') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> changePassword(@RequestBody Map<String, String> passwordData) {
        User user = getCurrentUser();
        userService.changePassword(
                user.getId(),
                passwordData.get("oldPassword"),
                passwordData.get("newPassword")
        );
        return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deactivateUser(@PathVariable Long id) {
        userService.deActiveUser(id);
        return ResponseEntity.ok(new MessageResponse("User deactivated successfully"));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> activateUser(@PathVariable Long id) {
        userService.activeUsers(id);
        return ResponseEntity.ok(new MessageResponse("User activated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }


    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
