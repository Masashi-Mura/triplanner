package com.example.triplanner.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.triplanner.entity.Trips;

public interface TripsRepository extends JpaRepository<Trips, Integer> {

	List<Trips> findAllByOrderByUpdatedAtDesc();//開発用のため公開設定を無視。　実際は公開のみを取得。

	Page<Trips> findByIdInOrderByUpdatedAtDesc(List<Integer> tripIds, Pageable pageable);

	@Query("SELECT id FROM Trips")
	List<Integer> findTripIdAll();

	@Query("SELECT t.id FROM Trips t WHERE (t.totalTripDays = :days) OR (:days = 5 AND t.totalTripDays >= 5)")
	List<Integer> findTripIdWithDays(@Param("days") Integer days);
}
