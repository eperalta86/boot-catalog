package cl.eperalta86.boot.catalog.service;

import cl.eperalta86.boot.catalog.domain.Platform;
import cl.eperalta86.boot.catalog.repository.PlatformRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlatformService {

    private final PlatformRepository repository;

    public PlatformService(PlatformRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Platform> findAll() {
        return repository.findAll();
    }
}
