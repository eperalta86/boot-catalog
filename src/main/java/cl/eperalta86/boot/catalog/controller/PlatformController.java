package cl.eperalta86.boot.catalog.controller;

import cl.eperalta86.boot.catalog.domain.Platform;
import cl.eperalta86.boot.catalog.service.PlatformService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platforms")
@CrossOrigin(origins = "http://localhost:4200")
public class PlatformController {

    private final PlatformService service;

    // Aunque no hay logica de negocio, hacemos la capa service para mantener
    // consistencia en la arquitectura.
    // No es overengineering.
    public PlatformController(PlatformService service) {
        this.service = service;
    }

    @GetMapping
    public List<Platform> getAll() {
        return service.findAll();
    }
}
