package com.api.crud.dto.response;

import com.api.crud.models.NotesModel;

import java.time.LocalDate;

public class NoteResponseDTO {
    private Long id;
    private String remoteId;
    private String title;
    private String note;
    private LocalDate date;
    private String userId;

    public NoteResponseDTO(NotesModel note) {
        this.id = note.getId();
        this.remoteId = note.getRemoteId();
        this.title = note.getTitle();
        this.note = note.getNote();
        this.date = note.getDate();
        this.userId = note.getUser().getId();
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
