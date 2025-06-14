package com.api.crud.manejar_errores;

public class TaskFoundException extends RuntimeException{
    public TaskFoundException(Long id){
        super("Tarea con el id" +id +" no se ha encontrado");
    }
}
