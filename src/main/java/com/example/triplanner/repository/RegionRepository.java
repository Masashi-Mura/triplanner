package com.example.triplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.triplanner.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Integer> {

	List<Region> findAllByOrderById();

}
