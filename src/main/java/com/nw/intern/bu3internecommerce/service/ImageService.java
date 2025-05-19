package com.nw.intern.bu3internecommerce.service;

import com.nw.intern.bu3internecommerce.dto.ImageDto;
import com.nw.intern.bu3internecommerce.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image getImageById(Long imageId);
    void deleteImageById(Long imageId);
    void updateImage(MultipartFile file, Long imageId);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
}
