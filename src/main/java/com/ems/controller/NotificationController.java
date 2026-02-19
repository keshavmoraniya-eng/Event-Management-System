package com.ems.controller;

import com.ems.dto.request.NotificationRequest;
import com.ems.dto.response.MessageResponse;
import com.ems.dto.response.NotificationResponse;
import com.ems.jwt.UserDetailsImpl;
import com.ems.model.User;
import com.ems.repository.UserRepository;
import com.ems.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create/notification")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest request){
        return ResponseEntity.ok(notificationService.createNotification(request));

    }

    @GetMapping("/my-notification")
    @PreAuthorize("hasRole('ATTENDEE') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(){
        User user=getCurrentUser();
        return ResponseEntity.ok(notificationService.getUserNotification(user));
    }

    @GetMapping("/unread")
    @PreAuthorize("hasRole('ATTENDEE') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications() {
        User user = getCurrentUser();
        return ResponseEntity.ok(notificationService.getUnreadNotification(user));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('ATTENDEE') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> markAsRead(@PathVariable Long id) {
        User user = getCurrentUser();
        notificationService.markAsRead(id, user);
        return ResponseEntity.ok(new MessageResponse("Notification marked as read"));
    }

    @PutMapping("/read-all")
    @PreAuthorize("hasRole('ATTENDEE') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> markAllAsRead() {
        User user = getCurrentUser();
        notificationService.markAllAsRead(user);
        return ResponseEntity.ok(new MessageResponse("All notifications marked as read"));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}
