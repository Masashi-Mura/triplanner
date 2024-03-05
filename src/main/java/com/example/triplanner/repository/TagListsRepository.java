package com.example.triplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.triplanner.entity.TagLists;

public interface TagListsRepository extends JpaRepository<TagLists, Integer> {
	
	List<TagLists> findByTripIdOrderById(Integer tripId);
	
}
