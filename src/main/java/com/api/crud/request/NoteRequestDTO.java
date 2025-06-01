package com.api.crud.request;

import jakarta.validation.constraints.NotBlank;

public class NoteRequestDTO {


    private String title;

    @NotBlank(message = "La nota no puede estar vacía")
    private String note;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}