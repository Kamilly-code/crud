package com.api.crud.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "notes")
public class NotesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String remoteId;

    private String title;

    @NotBlank(message = "La nota no puede estar vacía")
    private String note;

    private LocalDate date;

    @PrePersist
    public void setDefaultTitle() {
        if (this.title == null || this.title.trim().isEmpty()) {
            this.title = this.note.split("\\s+")[0];
        }
        if (this.date == null) {
            this.date = LocalDate.now();
        }
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
