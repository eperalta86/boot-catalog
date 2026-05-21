package cl.eperalta86.boot.catalog.service;

import cl.eperalta86.boot.catalog.config.CacheConfig;
import cl.eperalta86.boot.catalog.domain.Platform;
import cl.eperalta86.boot.catalog.dto.CreatePlatformRequest;
import cl.eperalta86.boot.catalog.dto.UpdatePlatformRequest;
import cl.eperalta86.boot.catalog.exception.BusinessException;
import cl.eperalta86.boot.catalog.exception.ResourceNotFoundException;
import cl.eperalta86.boot.catalog.repository.MediaItemRepository;
import cl.eperalta86.boot.catalog.repository.PlatformRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlatformService {

    private final PlatformRepository repository;
    private final MediaItemRepository mediaItemRepository;

    public PlatformService(PlatformRepository repository, MediaItemRepository mediaItemRepository) {
        this.repository = repository;
        this.mediaItemRepository = mediaItemRepository;
    }

    @Cacheable(CacheConfig.PLATFORMS_CACHE)
    @Transactional(readOnly = true)
    public List<Platform> findAll() {
        return repository.findAll();
    }

    @CacheEvict(value = CacheConfig.PLATFORMS_CACHE, allEntries = true)
    @Transactional
    public Platform create(CreatePlatformRequest request) {
        if (repository.existsByName(request.name())) {
            throw new BusinessException("Ya existe una plataforma con el nombre '" + request.name() + "'");
        }
        if (repository.existsByShortName(request.shortName())) {
            throw new BusinessException("Ya existe una plataforma con el nombre corto '" + request.shortName() + "'");
        }

        Platform platform = new Platform();
        platform.setName(request.name());
        platform.setShortName(request.shortName());
        return repository.save(platform);
    }

    @CacheEvict(value = CacheConfig.PLATFORMS_CACHE, allEntries = true)
    @Transactional
    public Platform update(Long id, UpdatePlatformRequest request) {
        Platform platform = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plataforma no encontrada: " + id));

        if (repository.existsByNameAndIdNot(request.name(), id)) {
            throw new BusinessException("Ya existe otra plataforma con el nombre '" + request.name() + "'");
        }
        if (repository.existsByShortNameAndIdNot(request.shortName(), id)) {
            throw new BusinessException("Ya existe otra plataforma con el nombre corto '" + request.shortName() + "'");
        }

        platform.setName(request.name());
        platform.setShortName(request.shortName());
        return repository.save(platform);
    }

    @CacheEvict(value = CacheConfig.PLATFORMS_CACHE, allEntries = true)
    @Transactional
    public void delete(Long id) {
        Platform platform = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plataforma no encontrada: " + id));

        if (mediaItemRepository.existsByPlatformId(id)) {
            throw new BusinessException("No se puede eliminar la plataforma porque tiene juegos asociados");
        }

        repository.delete(platform);
    }
}
