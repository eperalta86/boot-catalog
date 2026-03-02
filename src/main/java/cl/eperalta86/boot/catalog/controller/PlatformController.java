package cl.eperalta86.boot.catalog.controller;

import cl.eperalta86.boot.catalog.dto.PlatformResponse;
import cl.eperalta86.boot.catalog.service.PlatformService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platforms")
public class PlatformController {

    private final PlatformService service;

    public PlatformController(PlatformService service) {
        this.service = service;
    }

    @GetMapping
    public List<PlatformResponse> getAll() {
        return service.findAll().stream()
                .map(PlatformResponse::from)
                .toList();
    }
}
