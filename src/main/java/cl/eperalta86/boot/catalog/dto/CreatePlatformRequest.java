package cl.eperalta86.boot.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePlatformRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String name,

        @NotBlank(message = "El nombre corto es obligatorio")
        @Size(min = 1, max = 20, message = "El nombre corto debe tener entre 1 y 20 caracteres")
        String shortName
) {
}
