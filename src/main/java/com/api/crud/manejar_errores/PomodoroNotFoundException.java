package com.api.crud.manejar_errores;

public class PomodoroNotFoundException extends RuntimeException{
    public PomodoroNotFoundException(Long id){
        super("Pomodoro con el id" +id +" no se ha encontrado");
    }
}
