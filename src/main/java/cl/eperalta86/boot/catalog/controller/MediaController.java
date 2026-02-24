package cl.eperalta86.boot.catalog.controller;

import cl.eperalta86.boot.catalog.domain.MediaItem;
import cl.eperalta86.boot.catalog.domain.MediaItem.MediaStatus;
import cl.eperalta86.boot.catalog.service.MediaService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "http://localhost:4200")
public class MediaController {

    private final MediaService service;

    public MediaController(MediaService service) {
        this.service = service;
    }

    @GetMapping
    public List<MediaItem> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MediaItem getById(@PathVariable Long id) {
        return service.findById(id);
    }

    public record CreateMediaRequest(String title, Long platformId, MediaStatus status) {
    }

    @PostMapping
    public MediaItem create(@RequestBody CreateMediaRequest request) {
        return service.create(request.title(), request.platformId(), request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
