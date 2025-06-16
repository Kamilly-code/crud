package com.api.crud.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {
    @GetMapping("/")
    public String home() {
        return "API está funcionando!";
    }

    @GetMapping("/public/test")
    public String publicTest() {
        return "Ruta de prueba pública";
    }
}
