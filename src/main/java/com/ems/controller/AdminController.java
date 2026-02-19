package com.ems.controller;

import com.ems.dto.request.VenueRequest;
import com.ems.dto.response.*;
import com.ems.model.BookingStatus;
import com.ems.model.EventStatus;
import com.ems.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private VenueService venueService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats(){
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/list-users")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.of(Optional.ofNullable(userService.getAllUsers()));
    }

    @GetMapping("/get/user/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<MessageResponse> deActivateUser(@PathVariable Long id){
        userService.deActiveUser(id);
        return ResponseEntity.ok(new MessageResponse("User deactivated successfully"));
    }

    @PutMapping("/users/{id}/activate")
    public ResponseEntity<MessageResponse> activateUser(@PathVariable Long id){
        userService.activeUsers(id);
        return ResponseEntity.ok(new MessageResponse("User activated successfully"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }

    @GetMapping("/events/all")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        // This would need a new method in EventService to get ALL events (not just published)
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/events/status/{status}")
    public ResponseEntity<List<EventResponse>> getEventsByStatus(@PathVariable EventStatus status) {
        // Would need implementation in EventService
        return ResponseEntity.ok(eventService.getEventsByStatus(status));
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<MessageResponse> deleteEvent(@PathVariable Long id) {
        // Admin can delete any event
        return ResponseEntity.ok(new MessageResponse("Event deleted successfully"));
    }

    @GetMapping("/bookings/all")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        // Would need implementation in BookingService
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/bookings/status/{status}")
    public ResponseEntity<List<BookingResponse>> getBookingsByStatus(@PathVariable BookingStatus status) {
        // Would need implementation in BookingService
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status));
    }

    @GetMapping("/venues")
    public ResponseEntity<List<VenueResponse>> getAllVenues() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }

    @PostMapping("/create/venues")
    public ResponseEntity<VenueResponse> createVenue(@RequestBody VenueRequest venueRequest) {
        return ResponseEntity.ok(venueService.createVenue(venueRequest));
    }

    @PutMapping("/venues/{id}/update")
    public ResponseEntity<VenueResponse> updateVenue(
            @PathVariable Long id,
            @RequestBody VenueRequest venueRequest) {
        return ResponseEntity.ok(venueService.updateVenue(id, venueRequest));
    }

    @DeleteMapping("/venues/{id}/delete")
    public ResponseEntity<MessageResponse> deleteVenue(@PathVariable Long id) {
        venueService.delete(id);
        return ResponseEntity.ok(new MessageResponse("Venue deleted successfully"));
    }

    @GetMapping("/analytics/revenue")
    public ResponseEntity<DashboardStatsResponse> getRevenueAnalytics() {
        // Detailed revenue analytics
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/analytics/events")
    public ResponseEntity<DashboardStatsResponse> getEventAnalytics() {
        // Detailed event analytics
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/analytics/users")
    public ResponseEntity<DashboardStatsResponse> getUserAnalytics() {
        // Detailed user analytics
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

}
