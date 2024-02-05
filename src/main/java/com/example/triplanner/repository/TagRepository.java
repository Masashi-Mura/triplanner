package com.example.triplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.triplanner.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {
	
	List<Tag> findAllByOrderById();
	
}
