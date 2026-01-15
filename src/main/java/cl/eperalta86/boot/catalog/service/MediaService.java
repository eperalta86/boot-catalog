package cl.eperalta86.boot.catalog.service;

import cl.eperalta86.boot.catalog.domain.MediaItem;
import cl.eperalta86.boot.catalog.domain.MediaItem.MediaType;
import cl.eperalta86.boot.catalog.repository.MediaItemRepository;
import cl.eperalta86.boot.catalog.domain.MediaItem.MediaStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MediaService {

    private final MediaItemRepository repository;

    public MediaService(MediaItemRepository repository) {
        this.repository = repository;
    }

    // Spring Data JPA define los métodos.

    @Transactional(readOnly = true) 
    public List<MediaItem> findAll() {
        return repository.findAll();
    }

    @Transactional
    public MediaItem create(String title, MediaType type, MediaStatus status) {

        MediaItem newItem = new MediaItem(title, type, status);
        return repository.save(newItem);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}