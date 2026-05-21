package cl.eperalta86.boot.catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.eperalta86.boot.catalog.domain.MediaItem;
import cl.eperalta86.boot.catalog.domain.MediaItem.MediaStatus;
import cl.eperalta86.boot.catalog.dto.PlatformStatsResponse;

@Repository
public interface MediaItemRepository extends JpaRepository<MediaItem, Long>, JpaSpecificationExecutor<MediaItem> {

    boolean existsByPlatformId(Long platformId);

    @Query("""
            SELECT new cl.eperalta86.boot.catalog.dto.PlatformStatsResponse(
                p.id,
                p.name,
                p.shortName,
                COUNT(m),
                SUM(CASE WHEN m.status = :backlog THEN 1L ELSE 0L END),
                SUM(CASE WHEN m.status = :inProgress THEN 1L ELSE 0L END),
                SUM(CASE WHEN m.status = :finished THEN 1L ELSE 0L END)
            )
            FROM MediaItem m JOIN m.platform p
            GROUP BY p.id, p.name, p.shortName
            ORDER BY COUNT(m) DESC
            """)
    List<PlatformStatsResponse> getStatsByPlatform(
            @Param("backlog") MediaStatus backlog,
            @Param("inProgress") MediaStatus inProgress,
            @Param("finished") MediaStatus finished);
}
