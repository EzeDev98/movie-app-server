package com.movie.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "movies")
public class Movies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String titles;

    private String categories;

    private String years;

    private String origins;

    private String genres;

    @Column(length = 2000)
    private String descriptions;

    private String imageUrls;

    private String videoUrls;

    private LocalDateTime createdAt;
}
