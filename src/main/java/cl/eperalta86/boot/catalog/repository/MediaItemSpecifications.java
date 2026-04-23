package cl.eperalta86.boot.catalog.repository;

import cl.eperalta86.boot.catalog.domain.MediaItem;
import cl.eperalta86.boot.catalog.domain.MediaItem.MediaStatus;
import org.springframework.data.jpa.domain.Specification;

public class MediaItemSpecifications {

    private MediaItemSpecifications() {
    }

    public static Specification<MediaItem> titleContains(String title) {
        if (title == null || title.isBlank()) {
            return null;
        }
        String pattern = "%" + title.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), pattern);
    }

    public static Specification<MediaItem> hasPlatform(Long platformId) {
        if (platformId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("platform").get("id"), platformId);
    }

    public static Specification<MediaItem> hasStatus(MediaStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
