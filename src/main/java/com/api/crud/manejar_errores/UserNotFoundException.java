package com.api.crud.manejar_errores;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super("Usuario con el id" +id +" no se ha encontrado");
    }
}
