package cl.eperalta86.boot.catalog.controller;

import cl.eperalta86.boot.catalog.dto.CreateMediaItemRequest;
import cl.eperalta86.boot.catalog.dto.MediaItemResponse;
import cl.eperalta86.boot.catalog.dto.UpdateMediaItemRequest;
import cl.eperalta86.boot.catalog.service.MediaService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService service;

    public MediaController(MediaService service) {
        this.service = service;
    }

   @GetMapping
    public Page<MediaItemResponse> getAll(Pageable pageable) {
        return service.findAll(pageable)
                .map(MediaItemResponse::from);
    }

    @GetMapping("/{id}")
    public MediaItemResponse getById(@PathVariable Long id) {
        return MediaItemResponse.from(service.findById(id));
    }

   @PostMapping
    public MediaItemResponse create(@Valid @RequestBody CreateMediaItemRequest request) {
        return MediaItemResponse.from(
                service.create(request.title(), request.platformId(), request.status())
        );
    }

    @PutMapping("/{id}")
    public MediaItemResponse update(@PathVariable Long id,
                                    @Valid @RequestBody UpdateMediaItemRequest request) {
        return MediaItemResponse.from(
                service.update(id, request.title(), request.platformId(), request.status())
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
