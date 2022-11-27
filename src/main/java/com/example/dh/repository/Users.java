package com.example.dh.repository;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor
@Getter
@Table(name = "users")
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Column(name = "userId")
    private String userId;

    @Column(name = "pw")
    private String pw;

    @Column(name = "name")
    private String name;

    @Builder
    public Users(String userId, String pw, String name) {
        this.userId = userId;
        this.pw = pw;
        this.name = name;
    }

    public Users hashPw(PasswordEncoder pwEncoder) {
        this.pw = pwEncoder.encode(this.pw);
        return this;
    }

}
