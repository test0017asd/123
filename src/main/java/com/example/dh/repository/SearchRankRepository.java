package com.example.dh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRankRepository extends JpaRepository<SearchRank, Long> {

    public SearchRank findBySearch(String search);
}
