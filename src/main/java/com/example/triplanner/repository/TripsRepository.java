package com.example.triplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.triplanner.entity.Trips;

public interface TripsRepository extends JpaRepository<Trips, Integer> {

	List<Trips> findAllByOrderById();//開発用のため公開設定を無視。　実際は公開のみを取得。

}