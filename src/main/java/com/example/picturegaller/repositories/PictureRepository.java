package com.example.picturegaller.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.picturegaller.models.Picture;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    Optional<Picture> findByImageKey(String imageKey);

    Optional<Picture> findByImageName(String imageName);

    List<Picture> findByImageDescriptionContaining(String keyword); 

    List<Picture> findByImageUrl(String imageUrl);

    Page<Picture> findAll(Pageable page);

}
