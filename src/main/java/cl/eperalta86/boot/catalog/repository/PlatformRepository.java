package cl.eperalta86.boot.catalog.repository;

import cl.eperalta86.boot.catalog.domain.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
}
