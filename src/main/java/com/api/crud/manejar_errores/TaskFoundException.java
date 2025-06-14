package com.api.crud.manejar_errores;

public class TaskFoundException extends RuntimeException{
   /* public TaskFoundException(Long id){
        super("Tarea con el id" +id +" no se ha encontrado");
    }*/
    public TaskFoundException(String remoteId){
        super("Tarea con el id" +remoteId +" no se ha encontrado");
    }
}
