package com.ems.controller;

import com.ems.dto.request.BookingRequest;
import com.ems.dto.request.CheckInRequest;
import com.ems.dto.response.BookingResponse;
import com.ems.dto.response.MessageResponse;
import com.ems.jwt.UserDetailsImpl;
import com.ems.model.User;
import com.ems.repository.UserRepository;
import com.ems.service.BookingService;
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
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ATTENDEE') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest bookingRequest){

        User user=getCurrentUser();
        BookingResponse response=bookingService.createBooking(bookingRequest,user);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('ATTENDEE') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponse>> getMyBookings(){
        User user=getCurrentUser();
        return ResponseEntity.ok(bookingService.getUserBookings(user));
    }

    @GetMapping("/get-bookings/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id){
        User user=getCurrentUser();
        return ResponseEntity.ok(bookingService.getBookingById(id,user));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ATTENDEE') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> cancelBooking(@PathVariable Long id){
        User user=getCurrentUser();
        bookingService.cancelBooking(id,user);
        return ResponseEntity.ok(new MessageResponse("Booking cancel successfully"));
    }


    @PostMapping("/checkin")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> checkInAttendee(@Valid @RequestBody CheckInRequest checkInRequest){
        //Integrate with QR code scanning
        //Basic immplementation
        return ResponseEntity.ok(new MessageResponse("Check-In successful"));
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponse>> getEventBooking(@PathVariable Long eventId){
        return ResponseEntity.ok(List.of());
    }



    private User getCurrentUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(()->new RuntimeException("User not found"));
    }


}
