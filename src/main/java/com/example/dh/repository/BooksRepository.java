package com.example.dh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksRepository extends JpaRepository<Books, Long>  {

    public Books findByTitle(String title);

}
