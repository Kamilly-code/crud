package com.api.crud.controllers;

import com.api.crud.dto.request.PomodoroRequestDTO;
import com.api.crud.dto.response.PomodoroResponseDTO;
import com.api.crud.services.PomodoroService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pomodoros")
public class PomodoroController {

    private final PomodoroService pomodoroService;
    private static final String FIREBASE_USER_ID = "firebaseUserId";

    @Autowired
    public PomodoroController(PomodoroService pomodoroService) {
        this.pomodoroService = pomodoroService;

    }
    @GetMapping
    public ResponseEntity<PomodoroResponseDTO> getUserPomodoro(HttpServletRequest request) {
        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        return ResponseEntity.ok(pomodoroService.findByUserId(userId));
    }

    @GetMapping("/settings")
    public ResponseEntity<PomodoroResponseDTO> getPomodorosSettings(HttpServletRequest request) {
        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        PomodoroResponseDTO dto = pomodoroService.getPomodoroSettings(userId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    public ResponseEntity<PomodoroResponseDTO> insertPomodoro(
            @RequestBody PomodoroRequestDTO dto,
            HttpServletRequest request) {

        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        return ResponseEntity.ok(pomodoroService.insertPomodoro(dto, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PomodoroResponseDTO> updatePomodoro(
            @PathVariable Long id,
            @RequestBody PomodoroRequestDTO dto,
            HttpServletRequest request) {

        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        return ResponseEntity.ok(pomodoroService.updatePomodoro(id, dto, userId));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllPomodoroSettings(HttpServletRequest request) {
        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        pomodoroService.deleteAll(userId);
        return ResponseEntity.ok("Todas las configuraciones del pomodoro fueron borradas con éxito!");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
