package cl.eperalta86.boot.catalog.dto;

import cl.eperalta86.boot.catalog.domain.Platform;

public record PlatformResponse(
        Long id,
        String name,
        String shortName
) {
    public static PlatformResponse from(Platform platform) {
        return new PlatformResponse(
                platform.getId(),
                platform.getName(),
                platform.getShortName()
        );
    }
}
