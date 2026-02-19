package com.ems.service;

import com.ems.dto.request.EventRequest;
import com.ems.dto.response.EventResponse;
import com.ems.exception.ResourceNotFoundException;
import com.ems.exception.UnauthorizedException;
import com.ems.model.Event;
import com.ems.model.EventStatus;
import com.ems.model.User;
import com.ems.model.Venue;
import com.ems.repository.EventRepository;
import com.ems.repository.VenueRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Transactional
    public EventResponse createEvent(EventRequest request, User organizer) {
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDateTime(request.getStartDateTime());
        event.setEndDateTime(request.getEndDateTime());
        event.setCategory(request.getCategory());
        event.setMaxAttendees(request.getMaxAttendees());
        event.setImageUrl(request.getImageUrl());
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.UPCOMING);
        event.setIsPublished(request.getIsPublished() != null ? request.getIsPublished() : false);

        if (request.getVenueId() != null) {
            Venue venue = venueRepository.findById(request.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + request.getVenueId()));
            event.setVenue(venue);
        }

        Event savedEvent = eventRepository.save(event);
        return convertToResponse(savedEvent);
    }

    @Transactional
    public EventResponse updateEvent(Long id, EventRequest request, User user) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        // Check if user is the organizer
        if (!event.getOrganizer().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to update this event");
        }

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDateTime(request.getStartDateTime());
        event.setEndDateTime(request.getEndDateTime());
        event.setCategory(request.getCategory());
        event.setMaxAttendees(request.getMaxAttendees());
        event.setImageUrl(request.getImageUrl());

        if (request.getIsPublished() != null) {
            event.setIsPublished(request.getIsPublished());
        }

        if (request.getVenueId() != null) {
            Venue venue = venueRepository.findById(request.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
            event.setVenue(venue);
        }

        Event updatedEvent = eventRepository.save(event);
        return convertToResponse(updatedEvent);
    }

    public void deleteEvent(Long id, User user) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        if (!event.getOrganizer().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this event");
        }

        eventRepository.delete(event);
    }

    public List<EventResponse> getAllPublishedEvents() {
        return eventRepository.findByIsPublishedTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return convertToResponse(event);
    }

    public List<EventResponse> getEventsByOrganizer(User organizer) {
        return eventRepository.findByOrganizer(organizer).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    public List<EventResponse> searchEvents(String keyword) {
        return eventRepository.searchEvent(keyword).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getEventsByCategory(String category) {
        return eventRepository.findByCategory(category).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getUpcomingEvents() {
        return eventRepository.findByPublishedEventsByStatus(EventStatus.UPCOMING).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    public List<EventResponse> getAllEvents(){
        return eventRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getEventsByStatus(EventStatus status){
        return eventRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    private EventResponse convertToResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setStartDateTime(event.getStartDateTime());
        response.setEndDateTime(event.getEndDateTime());
        response.setCategory(event.getCategory());
        response.setStatus(event.getStatus());
        response.setMaxAttendees(event.getMaxAttendees());
        response.setCurrentAttendees(event.getCurrentAttendees());
        response.setImageUrl(event.getImageUrl());
        response.setIsPublished(event.getIsPublished());

        if (event.getVenue() != null) {
            response.setVenueName(event.getVenue().getName());
            response.setVenueAddress(event.getVenue().getAddress());
        }

        response.setOrganizerName(event.getOrganizer().getUsername());
        response.setCreatedAt(event.getCreatedAt());

        return response;
    }
}
