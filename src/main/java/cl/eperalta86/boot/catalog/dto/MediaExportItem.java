package cl.eperalta86.boot.catalog.dto;

import cl.eperalta86.boot.catalog.domain.MediaItem;

public record MediaExportItem(
        Long id,
        String title,
        String platformName,
        String status,
        String releaseDate
) {
    public static MediaExportItem from(MediaItem item) {
        return new MediaExportItem(
                item.getId(),
                item.getTitle(),
                item.getPlatform().getName(),
                item.getStatus().name(),
                item.getReleaseDate() != null ? item.getReleaseDate().toString() : ""
        );
    }
}
