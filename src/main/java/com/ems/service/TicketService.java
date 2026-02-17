package com.ems.service;

import com.ems.dto.request.TicketRequest;
import com.ems.dto.response.TicketResponse;
import com.ems.exception.ResourceNotFoundException;
import com.ems.model.Event;
import com.ems.model.Ticket;
import com.ems.repository.EventRepository;
import com.ems.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public TicketResponse createTicket(TicketRequest request){
        Event event=eventRepository.findById(request.getEventId())
                .orElseThrow(()->new ResourceNotFoundException("Event not found with id:"+request.getEventId()));

        Ticket ticket=new Ticket();
        ticket.setEvent(event);
        ticket.setTicketType(request.getTicketType());
        ticket.setDescription(request.getDescription());
        ticket.setPrice(request.getPrice());
        ticket.setAvailableQuantity(request.getAvailableQuantity());
        ticket.setSoldQuantity(0);
        ticket.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        Ticket savedTicket=ticketRepository.save(ticket);
        return convertToResponse(savedTicket);
    }

    @Transactional
    public TicketResponse updateTicket(Long id,TicketRequest request){
        Ticket ticket=ticketRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Ticket not found with id:"+id));

        ticket.setTicketType(request.getTicketType());
        ticket.setDescription(request.getDescription());
        ticket.setPrice(request.getPrice());
        ticket.setAvailableQuantity(request.getAvailableQuantity());

        if (request.getIsActive() != null){
            ticket.setIsActive(request.getIsActive());
        }

        Ticket updatedTicket=ticketRepository.save(ticket);
        return convertToResponse(updatedTicket);
    }

    public void deleteTicket(Long id){
        Ticket ticket=ticketRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Ticket not deleted with id:"+id));

        ticketRepository.delete(ticket);
    }

    public TicketResponse getTicketById(Long id){
        Ticket ticket=ticketRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Ticket not found with id:"+id));

        return convertToResponse(ticket);
    }

    public List<TicketResponse> getTicketByEvent(Long eventId){
        Event event=eventRepository.findById(eventId)
                .orElseThrow(()->new ResourceNotFoundException("Ticket not found by event id:"+eventId));

        return ticketRepository.findByEvent(event).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<TicketResponse> getActiveTicketsByEvent(Long eventId){
        Event event=eventRepository.findById(eventId)
                .orElseThrow(()->new ResourceNotFoundException("Active tickets by event not found with id:"+eventId));

        return ticketRepository.findByEvent(event).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private TicketResponse convertToResponse(Ticket ticket){
        TicketResponse response=new TicketResponse();
        response.setId(ticket.getId());
        response.setEventId(ticket.getEvent().getId());
        response.setEventTitle(ticket.getEvent().getTitle());
        response.setTicketType(ticket.getTicketType());
        response.setDescription(ticket.getDescription());
        response.setPrice(ticket.getPrice());
        response.setAvailableQuantity(ticket.getAvailableQuantity());
        response.setSoldQuantity(ticket.getSoldQuantity());
        response.setIsActive(ticket.getIsActive());
        response.setCreatedAt(ticket.getCreatedAt());
        return response;
    }
}
