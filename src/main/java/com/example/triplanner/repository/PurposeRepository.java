package com.example.triplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.triplanner.entity.Purpose;

public interface PurposeRepository extends JpaRepository<Purpose, Integer> {
	
	List<Purpose> findAllByOrderById();
	
}
