package com.ems.repository;

import com.ems.model.Booking;
import com.ems.model.BookingStatus;
import com.ems.model.Event;
import com.ems.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByEvent(Event event);
    List<Booking> findByStatus(BookingStatus status);
    Optional<Booking> findByBookingReference(String bookingReference);

    @Query("SELECT b FROM Booking b WHERE b.user= :user AND b.status= :status")
    List<Booking> findByUserAndStatus(@Param("user") User user,@Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.event = :event AND b.status = 'CONFIRMED'")
    List<Booking> findConfirmedBookingsByEvent(@Param("event") Event event);

}
