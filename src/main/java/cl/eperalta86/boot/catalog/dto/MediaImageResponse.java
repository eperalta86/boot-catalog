package cl.eperalta86.boot.catalog.dto;

import cl.eperalta86.boot.catalog.domain.ImageType;
import cl.eperalta86.boot.catalog.domain.MediaImage;

import java.time.LocalDateTime;

public record MediaImageResponse(
        Long id,
        ImageType imageType,
        String originalFileName,
        Long fileSize,
        String contentType,
        LocalDateTime createdAt
) {
    public static MediaImageResponse from(MediaImage image) {
        return new MediaImageResponse(
                image.getId(),
                image.getImageType(),
                image.getOriginalFileName(),
                image.getFileSize(),
                image.getContentType(),
                image.getCreatedAt()
        );
    }
}
