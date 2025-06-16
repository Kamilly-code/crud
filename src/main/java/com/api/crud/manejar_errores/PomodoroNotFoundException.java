package com.api.crud.manejar_errores;

public class PomodoroNotFoundException extends RuntimeException{
    public PomodoroNotFoundException(String remoteId) {
        super("Pomodoro con remoteId " + remoteId + " no se ha encontrado");
    }
}
