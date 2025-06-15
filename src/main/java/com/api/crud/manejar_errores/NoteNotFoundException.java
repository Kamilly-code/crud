package com.api.crud.manejar_errores;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(String remoteId) {
        super("Note com o remoteId " + remoteId + " não encontrada");
    }


}