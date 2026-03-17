package cl.eperalta86.boot.catalog.service;

import cl.eperalta86.boot.catalog.domain.ImageType;
import cl.eperalta86.boot.catalog.domain.MediaImage;
import cl.eperalta86.boot.catalog.domain.MediaItem;
import cl.eperalta86.boot.catalog.exception.BusinessException;
import cl.eperalta86.boot.catalog.exception.ResourceNotFoundException;
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
    private static final List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/webp");

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

        MediaItem mediaItem = mediaItemRepository.findById(mediaItemId)
                .orElseThrow(() -> new ResourceNotFoundException("MediaItem no encontrado: " + mediaItemId));

        if (imageRepository.existsByMediaItemIdAndImageType(mediaItemId, imageType)) {
            throw new BusinessException(
                    "Ya existe una imagen de tipo " + imageType + " para este item. Elimínala primero.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new BusinessException(
                    "Tipo de archivo no permitido. Solo se aceptan: JPEG, PNG y WebP");
        }

        Path itemDir = uploadDir.resolve(String.valueOf(mediaItemId));
        Files.createDirectories(itemDir);

        String extension = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + extension;
        Path filePath = itemDir.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);

        try {
            MediaImage image = new MediaImage();
            image.setImageType(imageType);
            image.setFilePath(filePath.toString());
            image.setOriginalFileName(file.getOriginalFilename());
            image.setFileSize(file.getSize());
            image.setContentType(file.getContentType());
            image.setMediaItem(mediaItem);

            return imageRepository.save(image);
        } catch (Exception e) {
            Files.deleteIfExists(filePath);
            throw e;
        }
    }
    
    @Transactional
    public void delete(Long imageId) throws IOException {
        MediaImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada: " + imageId));

        Files.deleteIfExists(Paths.get(image.getFilePath()));
        imageRepository.delete(image);
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @Transactional(readOnly = true)
    public MediaImage findById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada: " + imageId));
    }
}
