package com.api.crud.controllers;

import com.api.crud.dto.request.PomodoroRequestDTO;
import com.api.crud.dto.response.PomodoroResponseDTO;
import com.api.crud.models.PomodoroModel;
import com.api.crud.services.PomodoroService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<?> insertPomodoro(
            @RequestBody PomodoroRequestDTO dto,
            HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute(FIREBASE_USER_ID);
            PomodoroResponseDTO response = pomodoroService.insertPomodoro(dto, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Erro ao criar pomodoro",
                            "message", e.getMessage()));
        }

    }

    @PutMapping("/{remoteId}")
    public ResponseEntity<PomodoroResponseDTO> updatePomodoro(
            @PathVariable String remoteId,
            @RequestBody PomodoroRequestDTO dto,
            HttpServletRequest request) {

        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        return ResponseEntity.ok(pomodoroService.updatePomodoro(remoteId, dto, userId));
    }


    @DeleteMapping
    public ResponseEntity<String> deleteAllPomodoroSettings(HttpServletRequest request) {
        String userId = (String) request.getAttribute(FIREBASE_USER_ID);
        pomodoroService.deleteAll(userId);
        return ResponseEntity.ok("Todas as configurações do pomodoro foram borradas com sucesso!");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> pingPomodoro() {
        return ResponseEntity.ok("pong-pomodoro");
    }
}
