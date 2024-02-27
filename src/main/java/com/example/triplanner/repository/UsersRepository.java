package com.example.triplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.triplanner.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {
	
}
