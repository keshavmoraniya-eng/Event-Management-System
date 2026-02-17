package com.ems.repository;

import com.ems.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue,Long> {
    List<Venue> findByIsActiveTrue();
    List<Venue> findByCity(String city);
    List<Venue> findByCapacityGreaterThanEqual(Integer capacity);
}
