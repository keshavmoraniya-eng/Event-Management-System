package com.ems.controller;

import com.ems.dto.request.TicketRequest;
import com.ems.dto.response.MessageResponse;
import com.ems.dto.response.TicketResponse;
import com.ems.repository.TicketRepository;
import com.ems.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody TicketRequest ticketRequest){
        TicketResponse response=ticketService.createTicket(ticketRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/event/{eventId}")
    public ResponseEntity<List<TicketResponse>> getEventTickets(@PathVariable Long eventId){
        return ResponseEntity.ok(ticketService.getActiveTicketsByEvent(eventId));
    }

    @GetMapping("/public/ticket/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id){
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/event/tickets/{eventId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<List<TicketResponse>> getAllEventTickets(@PathVariable Long eventId){
        return ResponseEntity.ok(ticketService.getTicketByEvent(eventId));
    }

    @PutMapping("/update/ticket/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable Long id,@RequestBody TicketRequest request){
       TicketResponse response=ticketService.updateTicket(id,request);
       return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/ticket/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    private ResponseEntity<MessageResponse> deleteTicket(@PathVariable Long id){
        ticketService.deleteTicket(id);
        return ResponseEntity.ok(new MessageResponse("Ticket deleted successfully"));
    }


}
