package com.example.triplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.triplanner.entity.TagLists;

public interface TagListsRepository extends JpaRepository<TagLists, Integer> {

	List<TagLists> findByTripIdOrderById(Integer tripId);

	@Query("SELECT t.tripId FROM TagLists t WHERE t.tagId IN :tagIds GROUP BY t.tripId HAVING COUNT(t.tripId) = :tagLength")
	List<Integer> findTripIdWithAllTags(@Param("tagIds") List<Integer> tagIds, @Param("tagLength") Integer tagLength);

}
