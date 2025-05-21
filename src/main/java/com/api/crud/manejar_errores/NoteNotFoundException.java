package com.api.crud.manejar_errores;

public class NoteNotFoundException extends RuntimeException {
    // Construtor que aceita um ID
    public NoteNotFoundException(Long id) {
        super("Note con el id " + id + " no se ha encontrado");
    }

    // Construtor que aceita uma mensagem personalizada
    public NoteNotFoundException(String message) {
        super(message);
    }
}