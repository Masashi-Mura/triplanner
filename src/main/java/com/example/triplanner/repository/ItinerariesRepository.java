package com.example.triplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.triplanner.entity.Itinerary;

public interface ItinerariesRepository extends JpaRepository<Itinerary, Integer> {
	
}
