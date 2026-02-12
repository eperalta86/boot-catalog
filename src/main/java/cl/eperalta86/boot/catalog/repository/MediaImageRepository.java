package cl.eperalta86.boot.catalog.repository;

import cl.eperalta86.boot.catalog.domain.MediaImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaImageRepository extends JpaRepository<MediaImage, Long> {

    List<MediaImage> findByMediaItemId(Long mediaItemId);
}
