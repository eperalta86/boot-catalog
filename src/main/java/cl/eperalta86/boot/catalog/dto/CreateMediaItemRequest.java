package cl.eperalta86.boot.catalog.dto;

import cl.eperalta86.boot.catalog.domain.MediaItem.MediaStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateMediaItemRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(min = 2, max = 255, message = "El título debe tener entre 2 y 255 caracteres")
        String title,

        @NotNull(message = "La plataforma es obligatoria")
        Long platformId,

        @NotNull(message = "El estado es obligatorio")
        MediaStatus status
) {
}
