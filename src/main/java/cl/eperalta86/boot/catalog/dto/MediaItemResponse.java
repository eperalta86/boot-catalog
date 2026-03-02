package cl.eperalta86.boot.catalog.dto;

import cl.eperalta86.boot.catalog.domain.MediaItem;
import cl.eperalta86.boot.catalog.domain.MediaItem.MediaStatus;

import java.time.LocalDate;
import java.util.List;

public record MediaItemResponse(
        Long id,
        String title,
        PlatformResponse platform,
        MediaStatus status,
        LocalDate releaseDate,
        List<MediaImageResponse> images
) {
    public static MediaItemResponse from(MediaItem item) {
        return new MediaItemResponse(
                item.getId(),
                item.getTitle(),
                PlatformResponse.from(item.getPlatform()),
                item.getStatus(),
                item.getReleaseDate(),
                item.getImages().stream()
                        .map(MediaImageResponse::from)
                        .toList()
        );
    }
}
