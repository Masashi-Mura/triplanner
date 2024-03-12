package com.example.triplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.triplanner.entity.Itinerary;

public interface ItinerariesRepository extends JpaRepository<Itinerary, Integer> {

	List<Itinerary> findByTripIdOrderById(Integer tripId);

	//経由地でフィルタ
	@Query("SELECT i.tripId FROM Itinerary i WHERE i.departurePrefectureId IN :wIds AND i.rowSequence >= 2 "
			+ "GROUP BY i.tripId HAVING COUNT(i.tripId) = :wLength")
	List<Integer> findTripIdWithAllWaypoints(@Param("wIds") List<Integer> waypointIds, @Param("wLength") Integer waypointLength);
	
	//出発地でフィルタ
	@Query("SELECT i.tripId FROM Itinerary i WHERE i.departurePrefectureId = :sPoint AND i.rowSequence = 0")
	List<Integer> findTripIdWithStartPoint(@Param("sPoint") Integer startPoint);

}
