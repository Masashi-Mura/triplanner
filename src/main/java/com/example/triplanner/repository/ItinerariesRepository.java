package com.example.triplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.triplanner.entity.Itinerary;

public interface ItinerariesRepository extends JpaRepository<Itinerary, Integer> {

	List<Itinerary> findByTripIdOrderById(int tripId);

}
