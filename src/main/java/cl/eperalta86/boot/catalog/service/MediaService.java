package cl.eperalta86.boot.catalog.service;

import cl.eperalta86.boot.catalog.domain.MediaItem;
import cl.eperalta86.boot.catalog.domain.MediaItem.MediaStatus;
import cl.eperalta86.boot.catalog.domain.Platform;
import cl.eperalta86.boot.catalog.exception.ResourceNotFoundException;
import cl.eperalta86.boot.catalog.repository.MediaItemRepository;
import cl.eperalta86.boot.catalog.repository.MediaItemSpecifications;
import cl.eperalta86.boot.catalog.repository.PlatformRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;



@Service
public class MediaService {

    private final MediaItemRepository repository;
    private final PlatformRepository platformRepository;

    public MediaService(MediaItemRepository repository, PlatformRepository platformRepository) {
        this.repository = repository;
        this.platformRepository = platformRepository;
    }

     @Transactional(readOnly = true)
    public Page<MediaItem> findAll(String title, Long platformId, MediaStatus status, Pageable pageable) {
        Specification<MediaItem> spec = Specification.allOf(
                MediaItemSpecifications.titleContains(title),
                MediaItemSpecifications.hasPlatform(platformId),
                MediaItemSpecifications.hasStatus(status)
        );
        return repository.findAll(spec, pageable);
    }


    @Transactional
    public MediaItem create(String title, Long platformId, MediaStatus status) {
        Platform platform = platformRepository.findById(platformId)
                .orElseThrow(() -> new ResourceNotFoundException("Plataforma no encontrada: " + platformId));

        MediaItem newItem = new MediaItem(title, platform, status);
        return repository.save(newItem);
    }

    @Transactional
    public MediaItem update(Long id, String title, Long platformId, MediaStatus status) {
        MediaItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MediaItem no encontrado: " + id));

        Platform platform = platformRepository.findById(platformId)
                .orElseThrow(() -> new ResourceNotFoundException("Plataforma no encontrada: " + platformId));

        item.setTitle(title);
        item.setPlatform(platform);
        item.setStatus(status);

        return repository.save(item);
    }

    @Transactional
    public void delete(Long id) {
        MediaItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MediaItem no encontrado: " + id));
        repository.delete(item);
    }

    @Transactional(readOnly = true)
    public MediaItem findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MediaItem no encontrado: " + id));
    }
}
