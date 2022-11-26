package com.example.dh.repository;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "books")
@Entity
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Column(name = "title")
    private String title;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "contents")
    private String contents;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "authors")
    private String authors;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "datetime")
    private String datetime;

    @Column(name = "price")
    private String price;

    @Column(name = "salePrice")
    private String salePrice;

    @Builder
    public Books(Long no, String title, String thumbnail, String contents, String isbn, String authors, String publisher, String datetime, String price, String salePrice) {
        this.no = no;
        this.title = title;
        this.thumbnail = thumbnail;
        this.contents = contents;
        this.isbn = isbn;
        this.authors = authors;
        this.publisher = publisher;
        this.datetime = datetime;
        this.price = price;
        this.salePrice = salePrice;
    }

}