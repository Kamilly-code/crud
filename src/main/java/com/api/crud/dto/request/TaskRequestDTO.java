package com.api.crud.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public class TaskRequestDTO {
    @NotBlank(message = "La tarea es obligatoria")
    private String tarea;

    @NotNull(message = "El estado de la tarea es obligatorio")
    private Boolean isCompleted;
    private String remoteId;


    @NotNull(message = "La fecha no puede estar vacía")
    private LocalDate date;

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public  String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
