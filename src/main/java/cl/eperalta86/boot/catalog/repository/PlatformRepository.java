package cl.eperalta86.boot.catalog.repository;

import cl.eperalta86.boot.catalog.domain.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepository extends JpaRepository<Platform, Long> {

    boolean existsByName(String name);

    boolean existsByShortName(String shortName);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByShortNameAndIdNot(String shortName, Long id);
}
