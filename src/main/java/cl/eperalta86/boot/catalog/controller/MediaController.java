package cl.eperalta86.boot.catalog.controller;

import cl.eperalta86.boot.catalog.domain.MediaItem.MediaStatus;
import cl.eperalta86.boot.catalog.dto.CreateMediaItemRequest;
import cl.eperalta86.boot.catalog.dto.MediaItemResponse;
import cl.eperalta86.boot.catalog.dto.PlatformStatsResponse;
import cl.eperalta86.boot.catalog.dto.UpdateMediaItemRequest;
import cl.eperalta86.boot.catalog.service.MediaService;
import cl.eperalta86.boot.catalog.service.MediaExportService;
import cl.eperalta86.boot.catalog.service.MediaExportService.ExportResult;
import cl.eperalta86.boot.catalog.service.MediaExportService.Format;
import jakarta.validation.Valid;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService service;
    private final MediaExportService exportService;

    public MediaController(MediaService service, MediaExportService exportService) {
        this.service = service;
        this.exportService = exportService;
    }

    @GetMapping
    public Page<MediaItemResponse> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long platformId,
            @RequestParam(required = false) MediaStatus status,
            Pageable pageable) {
        return service.findAll(title, platformId, status, pageable).map(MediaItemResponse::from);
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

    @GetMapping("/stats/by-platform")
    public List<PlatformStatsResponse> getStatsByPlatform() {
        return service.getStatsByPlatform();
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> export(@RequestParam(required = false) String format) throws IOException {
        Format exportFormat = Format.fromString(format);
        ExportResult result = exportService.export(exportFormat);

        InputStream stream = Files.newInputStream(result.file());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(result.contentType()))
                .header("Content-Disposition", "attachment; filename=\"" + result.filename() + "\"")
                .contentLength(Files.size(result.file()))
                .body(new InputStreamResource(stream));
    }

}
