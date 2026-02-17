package com.ems.service;

import com.ems.dto.request.VenueRequest;
import com.ems.dto.response.VenueResponse;
import com.ems.exception.ResourceNotFoundException;
import com.ems.model.Venue;
import com.ems.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenueService {

    @Autowired
    private VenueRepository venueRepository;

    @Transactional
    public VenueResponse createVenue(VenueRequest venueRequest){
        Venue venue=new Venue();
        venue.setName(venueRequest.getName());
        venue.setAddress(venueRequest.getAddress());
        venue.setCity(venueRequest.getCity());
        venue.setState(venueRequest.getState());
        venue.setZipCode(venueRequest.getZipCode());
        venue.setCountry(venueRequest.getCountry());
        venue.setDescription(venueRequest.getDescription());
        venue.setCapacity(venueRequest.getCapacity());
        venue.setContactNumber(venueRequest.getContactNumber());
        venue.setContactEmail(venueRequest.getContactEmail());
        venue.setImageUrl(venueRequest.getImageUrl());
        venue.setIsActive(venueRequest.getIsActive() != null ? venueRequest.getIsActive() : true);

        Venue savedVenue=venueRepository.save(venue);
        return convertToResponse(savedVenue);

    }

    @Transactional
    public VenueResponse updateVenue(Long id,VenueRequest venueRequest){
        Venue venue=venueRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Venue not found"));

        venue.setName(venueRequest.getName());
        venue.setAddress(venueRequest.getAddress());
        venue.setCity(venueRequest.getCity());
        venue.setState(venueRequest.getState());
        venue.setZipCode(venueRequest.getZipCode());
        venue.setCountry(venueRequest.getCountry());
        venue.setDescription(venueRequest.getDescription());
        venue.setCapacity(venueRequest.getCapacity());
        venue.setContactNumber(venueRequest.getContactNumber());
        venue.setContactEmail(venueRequest.getContactEmail());
        venue.setImageUrl(venueRequest.getImageUrl());

        if (venueRequest.getIsActive() != null){
            venue.setIsActive(venueRequest.getIsActive());
        }

        Venue updatedVenue=venueRepository.save(venue);
        return convertToResponse(updatedVenue);
    }


    public void delete(Long id){
        Venue venue=venueRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Venue not found with id:"+id));

        venueRepository.delete(venue);
    }

    public VenueResponse getVenueById(Long id){
        Venue venue=venueRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Venue not found with id:"+id));

        return convertToResponse(venue);
    }

    public List<VenueResponse> getAllVenues(){
        return venueRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<VenueResponse> getActiveVenues(){
        return venueRepository.findByIsActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<VenueResponse> getVenuesByCity(String city){
        return venueRepository.findByCity(city).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<VenueResponse> getVenuesByCapacity(Integer minCapacity){
        return venueRepository.findByCapacityGreaterThanEqual(minCapacity).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private VenueResponse convertToResponse(Venue venue){
        VenueResponse response=new VenueResponse();
        response.setId(venue.getId());
        response.setName(venue.getName());
        response.setAddress(venue.getAddress());
        response.setCity(venue.getCity());
        response.setState(venue.getState());
        response.setZipCode(venue.getZipCode());
        response.setCountry(venue.getCountry());
        response.setDescription(venue.getDescription());
        response.setCapacity(venue.getCapacity());
        response.setContactNumber(venue.getContactNumber());
        response.setContactEmail(venue.getContactEmail());
        response.setImageUrl(venue.getImageUrl());
        response.setIsActive(venue.getIsActive());
        response.setCreatedAt(venue.getCreatedAt());
        response.setUpdatedAt(venue.getUpdatedAt());
        return response;
    }

}
