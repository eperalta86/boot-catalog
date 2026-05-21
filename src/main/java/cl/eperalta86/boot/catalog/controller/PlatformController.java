package cl.eperalta86.boot.catalog.controller;

import cl.eperalta86.boot.catalog.domain.Platform;
import cl.eperalta86.boot.catalog.dto.CreatePlatformRequest;
import cl.eperalta86.boot.catalog.dto.PlatformResponse;
import cl.eperalta86.boot.catalog.dto.UpdatePlatformRequest;
import cl.eperalta86.boot.catalog.service.PlatformService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/platforms")
public class PlatformController {

    private final PlatformService service;

    public PlatformController(PlatformService service) {
        this.service = service;
    }

    @GetMapping
    public List<PlatformResponse> getAll() {
        return service.findAll().stream()
                .map(PlatformResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<PlatformResponse> create(@Valid @RequestBody CreatePlatformRequest request) {
        Platform created = service.create(request);
        PlatformResponse body = PlatformResponse.from(created);
        URI location = URI.create("/api/platforms/" + created.getId());
        return ResponseEntity.created(location).body(body);
    }

    @PutMapping("/{id}")
    public PlatformResponse update(@PathVariable Long id, @Valid @RequestBody UpdatePlatformRequest request) {
        Platform updated = service.update(id, request);
        return PlatformResponse.from(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
