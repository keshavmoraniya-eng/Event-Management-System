package com.ems.repository;

import com.ems.model.Event;
import com.ems.model.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByOrganizer(String organizer);
    List<Event> findByStatus(EventStatus status);
    List<Event> findByIsPublishedTrue();
    List<Event> findByCategory(String category);

    @Query("SELECT e FROM Event e WHERE e.isPublished = true AND e.status = :status")
    List<Event> findByPublishedEventsByStatus(@Param("status") EventStatus status);

    @Query("SELECT e FROM Event e WHERE e.startDateTime >= :startDate AND e.endDateTime <= :endDate")
    List<Event> findEventsBetweenDates(@Param("startDate")LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate);

    @Query("""
    SELECT e FROM Event e
    WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))"""
    )
    List<Event> searchEvent(@Param("keyword") String keyword);

}
