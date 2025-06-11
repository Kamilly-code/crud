package com.api.crud.manejar_errores;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String id){
        super("Usuario con el id" +id +" no se ha encontrado");
    }
}
