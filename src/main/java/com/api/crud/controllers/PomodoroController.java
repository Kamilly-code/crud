package com.api.crud.controllers;

import com.api.crud.dto.request.PomodoroRequestDTO;
import com.api.crud.dto.response.PomodoroResponseDTO;
import com.api.crud.services.PomodoroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pomodoros")
public class PomodoroController {


    private final PomodoroService pomodoroService;

    @Autowired
    public PomodoroController(PomodoroService pomodoroService) {
        this.pomodoroService = pomodoroService;
    }

    @GetMapping
    public ResponseEntity<PomodoroResponseDTO> getPomodorosSettings(){
        PomodoroResponseDTO dto = pomodoroService.getPomodoroSettings();
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    public ResponseEntity<PomodoroResponseDTO> insertPomodoro(@RequestBody PomodoroRequestDTO dto) {
        return ResponseEntity.ok(pomodoroService.insertPomodoro(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PomodoroResponseDTO> updatePomodoro(@PathVariable Long id,
                                                              @RequestBody PomodoroRequestDTO dto) {
        return ResponseEntity.ok(pomodoroService.updatePomodoro(id, dto));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllPomodoroSettings() {
        pomodoroService.deleteAll();
        return ResponseEntity.ok("Todas las configuraciones del pomodoro fueron borradas con suceso!");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

}
