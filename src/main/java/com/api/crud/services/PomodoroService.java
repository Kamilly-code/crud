package com.api.crud.services;

import com.api.crud.manejar_errores.PomodoroNotFoundException;
import com.api.crud.models.PomodoroModel;
import com.api.crud.repositories.PomodoroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PomodoroService {

    @Autowired
    private PomodoroRepository pomodoroRepository;

    public ArrayList<PomodoroModel> getPomodoros(){
        return (ArrayList<PomodoroModel>)pomodoroRepository.findAll();
    }

    public PomodoroModel insertPomodoro(PomodoroModel pomodoro){
        return pomodoroRepository.save(pomodoro);
    }

    public PomodoroModel updatePomodoro(Long id, PomodoroModel request) {
        PomodoroModel existingPomodoro = pomodoroRepository.findById(id)
                .orElseThrow(() -> new PomodoroNotFoundException(id));

        existingPomodoro.setFocusTime(request.getFocusTime());
        existingPomodoro.setShortBreakTime(request.getShortBreakTime());
        existingPomodoro.setLongBreakTime(request.getLongBreakTime());
        existingPomodoro.setRounds(request.getRounds());

        return pomodoroRepository.save(existingPomodoro);
    }

    public PomodoroModel getPomodoroSettings() {
        return pomodoroRepository.findAll()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public void deleteAll() {
        pomodoroRepository.deleteAll();
    }

}
