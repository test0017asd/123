package com.example.dh.repository;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "searchRank")
@Entity
public class SearchRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Column(name = "search")
    private String search;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hit")
    private int hit;

    @Builder
    public SearchRank(String search) {
        this.search = search;
    }

    public SearchRank updateHit(int hit) {
        this.hit = hit + 1;
        return this;
    }
}
