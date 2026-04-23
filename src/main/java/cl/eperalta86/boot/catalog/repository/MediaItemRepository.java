package cl.eperalta86.boot.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import cl.eperalta86.boot.catalog.domain.MediaItem;

@Repository
public interface MediaItemRepository extends JpaRepository<MediaItem, Long>, JpaSpecificationExecutor<MediaItem> {

}
