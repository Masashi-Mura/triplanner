package com.example.triplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.triplanner.entity.Trips;

public interface TripsRepository extends JpaRepository<Trips, Integer> {
	
}
