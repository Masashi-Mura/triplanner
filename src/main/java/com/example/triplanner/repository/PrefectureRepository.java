package com.example.triplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.triplanner.entity.Prefecture;

public interface PrefectureRepository extends JpaRepository<Prefecture, Integer> {
	
	List<Prefecture> findAllByOrderById();
	
}
