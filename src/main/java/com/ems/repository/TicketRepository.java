package com.ems.repository;

import com.ems.model.Event;
import com.ems.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEvent(Event event);
    List<Ticket> findByEventAndIsActiveTrue(Event event);
}
