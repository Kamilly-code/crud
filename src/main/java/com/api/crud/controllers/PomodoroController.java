package com.api.crud.controllers;

import com.api.crud.models.PomodoroModel;
import com.api.crud.services.PomodoroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/pomodoros")
public class PomodoroController {

    @Autowired
    private PomodoroService pomodoroService;

    @GetMapping
    public ResponseEntity<PomodoroModel> getPomodorosSettings(){
        PomodoroModel pomodoro = pomodoroService.getPomodoroSettings();
        if (pomodoro != null) {
            return ResponseEntity.ok(pomodoro);
        }else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    public ResponseEntity<PomodoroModel> insertPomodoro(@RequestBody PomodoroModel pomodoro) {
        return ResponseEntity.ok(pomodoroService.insertPomodoro(pomodoro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PomodoroModel> updatePomodoro(@PathVariable Long id, @RequestBody PomodoroModel pomodoro){
        return ResponseEntity.ok(pomodoroService.updatePomodoro(id,pomodoro));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllPomodoroSettings() {
        pomodoroService.deleteAll();
        return ResponseEntity.ok("Todas las configuraciones del pomodoro fueron borradas con suceso!");
    }

}
