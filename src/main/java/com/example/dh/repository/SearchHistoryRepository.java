package com.example.dh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistroy, Long> {

    public List<SearchHistroy> findAllByUserNo(Long userNo);

    public List<SearchHistroy> findAllByWordAndUserNo(String word, Long userNo);

    public SearchHistroy findByWordAndUserNo(String word, Long userNo);
}
