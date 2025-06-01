package com.api.crud.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class TaskRequestDTO {
    @NotBlank(message = "La tarea es obligatoria")
    private String tarea;

    @NotNull(message = "El estado de la tarea es obligatorio")
    private Boolean isCompleted;

    public String getTarea() {
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
}
