package com.example.triplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.triplanner.entity.TagLists;

public interface TagListsRepository extends JpaRepository<TagLists, Integer> {
	
}
