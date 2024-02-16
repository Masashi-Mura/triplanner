package com.example.triplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.triplanner.entity.PublicOption;

public interface PublicOptionRepository extends JpaRepository<PublicOption, Integer> {
	
	List<PublicOption> findAllByOrderById();
	
}
