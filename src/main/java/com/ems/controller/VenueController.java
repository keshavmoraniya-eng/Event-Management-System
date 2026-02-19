package com.ems.controller;

import com.ems.dto.request.VenueRequest;
import com.ems.dto.response.MessageResponse;
import com.ems.dto.response.VenueResponse;
import com.ems.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/venues")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @GetMapping("/public/get-venues")
    public ResponseEntity<List<VenueResponse>> getAllVenues(){
        return ResponseEntity.ok(venueService.getAllVenues());
    }

    @GetMapping("/public/get-active-venues")
    public ResponseEntity<List<VenueResponse>> getActiveVenues(){
        return ResponseEntity.ok(venueService.getActiveVenues());
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<VenueResponse> getVenueById(@PathVariable Long id){
        return ResponseEntity.ok(venueService.getVenueById(id));
    }

    @GetMapping("/public/city/{city}")
    public ResponseEntity<List<VenueResponse>> getVenuesByCity(@PathVariable String city){
        return ResponseEntity.ok(venueService.getVenuesByCity(city));
    }

    @GetMapping("/public/capacity")
    public ResponseEntity<List<VenueResponse>> getVenuesByCapacity(@RequestParam Integer minCapacity){
        return ResponseEntity.ok(venueService.getVenuesByCapacity(minCapacity));
    }

    @PostMapping("/create-venue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VenueResponse> createVenue(@Valid @RequestBody VenueRequest request){
        return ResponseEntity.ok(venueService.createVenue(request));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VenueResponse> updateVenue(@PathVariable Long id,@Valid @RequestBody VenueRequest request){
        VenueResponse response=venueService.updateVenue(id,request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-venue/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteVenue(@PathVariable Long id){
        venueService.delete(id);
        return ResponseEntity.ok(new MessageResponse("Venue deleted successfully"));
    }


}
