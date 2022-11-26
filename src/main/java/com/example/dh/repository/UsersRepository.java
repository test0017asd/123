package com.example.dh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

    public Users findByUserId(String id);

}
