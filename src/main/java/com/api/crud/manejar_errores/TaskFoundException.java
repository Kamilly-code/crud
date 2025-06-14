package com.api.crud.manejar_errores;

public class TaskFoundException extends RuntimeException{
    public TaskFoundException(String remoteId){
        super("Tarea con el id" +remoteId +" no se ha encontrado");
    }
}
