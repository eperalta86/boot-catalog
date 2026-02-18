package cl.eperalta86.boot.catalog.service;

import cl.eperalta86.boot.catalog.domain.ImageType;
import cl.eperalta86.boot.catalog.domain.MediaImage;
import cl.eperalta86.boot.catalog.domain.MediaItem;
import cl.eperalta86.boot.catalog.repository.MediaImageRepository;
import cl.eperalta86.boot.catalog.repository.MediaItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class MediaImageService {

    private final MediaImageRepository imageRepository;
    private final MediaItemRepository mediaItemRepository;
    private final Path uploadDir;

    public MediaImageService(MediaImageRepository imageRepository,
                             MediaItemRepository mediaItemRepository,
                             @Value("${app.upload.dir:uploads}") String uploadDir) {

        this.imageRepository = imageRepository;
        this.mediaItemRepository = mediaItemRepository;
        this.uploadDir = Paths.get(uploadDir);
    }

    @Transactional(readOnly = true)
    public List<MediaImage> findByMediaItemId(Long mediaItemId) {
        return imageRepository.findByMediaItemId(mediaItemId);
    }

    @Transactional
    public MediaImage upload(Long mediaItemId, ImageType imageType, MultipartFile file) throws IOException {

        MediaItem mediaItem = mediaItemRepository.findById(mediaItemId).orElseThrow(() -> new RuntimeException("MediaItem no encontrado: " + mediaItemId));

        // Crear directorio si no existe: uploads/{mediaItemId}/
        Path itemDir = uploadDir.resolve(String.valueOf(mediaItemId));
        Files.createDirectories(itemDir);

        // Generar nombre único para evitar colisiones
        String extension = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + extension;

        // Guardar archivo en disco
        Path filePath = itemDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        // Crear registro en BD
        MediaImage image = new MediaImage();
        image.setImageType(imageType);
        image.setFilePath(filePath.toString());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setFileSize(file.getSize());
        image.setContentType(file.getContentType());
        image.setMediaItem(mediaItem);

        return imageRepository.save(image);
    }

    @Transactional
    public void delete(Long imageId) throws IOException {
        MediaImage image = imageRepository.findById(imageId).orElseThrow(() -> new RuntimeException("Imagen no encontrada: " + imageId));

        // Eliminar archivo del disco
        Files.deleteIfExists(Paths.get(image.getFilePath()));

        // Eliminar registro de BD
        imageRepository.delete(image);
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}