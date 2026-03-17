package cl.eperalta86.boot.catalog.dto;

public record AuthResponse(
        String token,
        String username
    ) {

}