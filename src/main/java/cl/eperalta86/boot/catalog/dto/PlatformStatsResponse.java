package cl.eperalta86.boot.catalog.dto;

public record PlatformStatsResponse(
        Long platformId,
        String platformName,
        String platformShortName,
        Long totalGames,
        Long backlogCount,
        Long inProgressCount,
        Long finishedCount
) {
}
