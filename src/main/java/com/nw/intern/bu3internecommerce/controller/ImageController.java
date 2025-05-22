package com.nw.intern.bu3internecommerce.controller;

import com.nw.intern.bu3internecommerce.dto.ImageDto;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;
import com.nw.intern.bu3internecommerce.entity.Image;
import com.nw.intern.bu3internecommerce.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("admin/image/upload")
    public ApiResponse<List<ImageDto>> uploadImages(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("productId") Long productId) {
        List<ImageDto> imageDto = imageService.saveImages(productId, files);
        return ApiResponse.ok(imageDto);
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + image.getFileName() + "\"").body(resource);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("admin/image/{imageId}/update")
    public ApiResponse<Void> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
        imageService.updateImage(file, imageId);
        return ApiResponse.ok();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("admin/image/{imageId}/delete")
    public ApiResponse<Void> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImageById(imageId);
        return ApiResponse.ok();
    }
}
