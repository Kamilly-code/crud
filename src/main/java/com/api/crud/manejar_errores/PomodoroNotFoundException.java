package com.api.crud.manejar_errores;

public class PomodoroNotFoundException extends RuntimeException{
    public PomodoroNotFoundException(String remoteId) {
        super("Pomodoro com o remoteId " + remoteId + " não encontrado");
    }
}
