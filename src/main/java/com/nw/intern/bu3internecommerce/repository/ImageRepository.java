package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Optional<Image> findById(Long id);

    void deleteById(Long imageId);
}
