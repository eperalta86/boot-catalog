package cl.eperalta86.boot.catalog.controller;

import cl.eperalta86.boot.catalog.domain.ImageType;
import cl.eperalta86.boot.catalog.domain.MediaImage;
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
@CrossOrigin(origins = "http://localhost:4200")
public class MediaImageController {

    private final MediaImageService service;

    public MediaImageController(MediaImageService service) {
        this.service = service;
    }

    @GetMapping
    public List<MediaImage> getAll(@PathVariable Long mediaItemId) {
        return service.findByMediaItemId(mediaItemId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MediaImage upload(@PathVariable Long mediaItemId,
            @RequestParam("imageType") ImageType imageType,
            @RequestParam("file") MultipartFile file) throws IOException {
        return service.upload(mediaItemId, imageType, file);
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
        MediaImage image = service.findById(imageId);
        return Files.readAllBytes(Paths.get(image.getFilePath()));
    }
}
