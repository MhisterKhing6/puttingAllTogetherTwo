package com.example.picturegaller.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 255)
    private String imageKey;

    @Column(length = 500) // Increase length for descriptions
    private String imageDescription;

    @Column(length = 255)
    private String imageName;

    @Column(length = 2083) // Max URL length per RFC 2616
    private String imageUrl;

    public Picture(String imageKey, String imageDescription, String imageName, String imageUrl) {
        this.imageDescription = imageDescription;
        this.imageKey = imageKey;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }
}
