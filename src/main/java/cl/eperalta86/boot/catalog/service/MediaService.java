package cl.eperalta86.boot.catalog.service;

import cl.eperalta86.boot.catalog.domain.MediaItem;
import cl.eperalta86.boot.catalog.domain.MediaItem.MediaStatus;
import cl.eperalta86.boot.catalog.domain.Platform;
import cl.eperalta86.boot.catalog.repository.MediaItemRepository;
import cl.eperalta86.boot.catalog.repository.PlatformRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MediaService {

    private final MediaItemRepository repository;
    private final PlatformRepository platformRepository;

    public MediaService(MediaItemRepository repository, PlatformRepository platformRepository) {
        this.repository = repository;
        this.platformRepository = platformRepository;
    }

    @Transactional(readOnly = true)
    public List<MediaItem> findAll() {
        return repository.findAll();
    }

    @Transactional
    public MediaItem create(String title, Long platformId, MediaStatus status) {
        Platform platform = platformRepository.findById(platformId)
                .orElseThrow(() -> new RuntimeException("Plataforma no encontrada: " + platformId));

        MediaItem newItem = new MediaItem(title, platform, status);
        return repository.save(newItem);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public MediaItem findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("MediaItem no encontrado: " + id));
    }
}
