package com.example.dh.repository;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "searchHistory")
@Entity
public class SearchHistroy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Column(name="userNo")
    private Long userNo;

    @Column(name="word")
    private String word;

    @Column(name="date")
    private String date;

    @Builder
    public SearchHistroy(String word, Long userNo) {
        this.word = word;
        this.userNo = userNo;
    }

    public SearchHistroy updateDate(String date) {
        this.date = date;
        return this;
    }
}
