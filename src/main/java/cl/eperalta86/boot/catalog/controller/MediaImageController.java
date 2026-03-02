package cl.eperalta86.boot.catalog.controller;

import cl.eperalta86.boot.catalog.domain.ImageType;
import cl.eperalta86.boot.catalog.dto.MediaImageResponse;
import cl.eperalta86.boot.catalog.service.MediaImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/media/{mediaItemId}/images")
public class MediaImageController {

    private final MediaImageService service;

    public MediaImageController(MediaImageService service) {
        this.service = service;
    }

    @GetMapping
    public List<MediaImageResponse> getAll(@PathVariable Long mediaItemId) {
        return service.findByMediaItemId(mediaItemId).stream()
                .map(MediaImageResponse::from)
                .toList();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MediaImageResponse upload(@PathVariable Long mediaItemId,
            @RequestParam("imageType") ImageType imageType,
            @RequestParam("file") MultipartFile file) throws IOException {
        return MediaImageResponse.from(service.upload(mediaItemId, imageType, file));
    }

    @DeleteMapping("/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long mediaItemId,
            @PathVariable Long imageId) throws IOException {
        service.delete(imageId);
    }

    @GetMapping("/{imageId}/file")
    public byte[] getFile(@PathVariable Long mediaItemId,
            @PathVariable Long imageId) throws IOException {
        return Files.readAllBytes(Paths.get(service.findById(imageId).getFilePath()));
    }
}
