package com.ems.service;

import com.ems.dto.request.BookingRequest;
import com.ems.dto.response.BookingResponse;
import com.ems.exception.BadRequestException;
import com.ems.exception.ResourceNotFoundException;
import com.ems.model.*;
import com.ems.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public BookingResponse createBooking(BookingRequest request, User user){
        //Validate event
        Event event=eventRepository.findById(request.getEventId())
                .orElseThrow(()->new ResourceNotFoundException("Event not found."));

        //Validate ticket
        Ticket ticket=ticketRepository.findById(request.getTicketId())
                .orElseThrow(()->new ResourceNotFoundException("Ticket not found."));

        //Check ticket availability
        if (ticket.getAvailableQuantity() < request.getQuantity()){
            throw new BadRequestException("Not enough tickets available.");
        }

        //Check event capacity
        if (event.getMaxAttendees() !=null && event.getCurrentAttendees() + request.getQuantity() > event.getMaxAttendees()){
            throw new BadRequestException("Event is full");
        }

        //Create ticket
        Booking booking=new Booking();
        booking.setBookingReference(generateBookingReference());
        booking.setUser(user);
        booking.setEvent(event);
        booking.setTicket(ticket);
        booking.setQuantity(request.getQuantity());
        booking.setTotalAmount(ticket.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        booking.setStatus(BookingStatus.PENDING);

        //Update Ticket availability
        ticket.setSoldQuantity(ticket.getSoldQuantity()+request.getQuantity());
        ticket.setAvailableQuantity(ticket.getAvailableQuantity()-request.getQuantity());
        ticketRepository.save(ticket);

        //Update event attendance
        event.setCurrentAttendees(event.getCurrentAttendees()+request.getQuantity());
        eventRepository.save(event);

        Booking savedBooking=bookingRepository.save(booking);
        return convertToResponse(savedBooking);

    }


    public List<BookingResponse> getUserBookings(User user){
        return bookingRepository.findByUser(user).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingById(Long id,User user){
        Booking booking=bookingRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())){
            throw new BadRequestException("Access denied");
        }

        return convertToResponse(booking);
    }




    private BookingResponse convertToResponse(Booking booking){
        BookingResponse response=new BookingResponse();
        response.setId(booking.getId());
        response.setBookingReference(booking.getBookingReference());
        response.setQuantity(booking.getQuantity());
        response.setTotalAmount(booking.getTotalAmount());
        response.setStatus(booking.getStatus());
        response.setQrCode(booking.getQrCode());
        response.setCheckedIn(booking.getCheckedIn());

        if (booking.getEvent() != null) {
            response.setEventTitle(booking.getEvent().getTitle());
            response.setEventStartDateTime(booking.getEvent().getStartDateTime());
        }

        if (booking.getTicket() != null) {
            response.setTicketType(booking.getTicket().getTicketType());
        }

        response.setCreatedAt(booking.getCreatedAt());

        return response;
    }

}
